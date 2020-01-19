
package com.example.nliot.MQTT;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.Constants;
import com.example.nliot.controller.ItemController;
import com.example.nliot.controller.ItemManagerController;
import com.example.nliot.controller.MQTTController;
import com.example.nliot.entity.Item;
import com.example.nliot.entity.ItemManager;
import com.example.nliot.service.MQTTPublishService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveClientCallBack implements MqttCallback {

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("clientMqtt 连接断开，可以做重连");
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("clientMqtt deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        String str = new String(message.getPayload());
        System.out.println("clientMqtt 接收消息主题 : " + topic);
        System.out.println("clientMqtt 接收消息Qos : " + message.getQos());
        System.out.println("clientMqtt 接收消息内容 : " + new String(message.getPayload()));

        //处理消息
        JSONObject json = JSONObject.parseObject(str);
        String contentType = json.getString("消息类型");
        if(contentType != null){
            String dataStr = json.getString("消息内容");
            JSONObject data = JSONObject.parseObject(dataStr);
            String target = data.getString("发送地址");
            if(contentType.equals("节点列表")){
                //返回全部的节点（物关+物设备）
                List<Item> itemList = ItemController.MQTTgetAllItemNode();
                List<ItemManager> itemManagerList = ItemManagerController.MQTTgetAllItemManagerNode();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("物设备",itemList);
                map.put("物关",itemManagerList);
                JSONObject result = new JSONObject(map);
                MQTTController.sendDebugInfo(contentType, result);
            }
            else if(contentType.equals("节点查询")){
                //返回查询的单一节点（物关+物设备）
                String nodeName = data.getString("目标名称");
                Item item = ItemController.MQTTgetOneItemNode(nodeName);
                ItemManager itemManager = ItemManagerController.MQTTgetOneItemManagerNode(nodeName);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("物设备",item);
                map.put("物关",itemManager);
                JSONObject result = new JSONObject(map);
                MQTTController.sendDebugInfo(contentType, result);
            }
            else {
                //节点搜索，查询当前物关下nodeName的物关或者物设备
                String nodeName = data.getString("目标名称");
                //判断是否是本物关下的节点
                if(nodeName.contains(Constants.physicalAddress)){
                    //查询的节点名字包含当前物关名称，说明该节点在本物关下
                    //检查物设备节点
                    List<Item> items = ItemController.MQTTgetAllItemNode();
                    for(int i = 0; i < items.size(); ++i){
                        Item item = items.get(i);
                        if((item.getManagerName() + item.getName()).equals(nodeName)){
                            //find the item
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("物设备",item);
                            JSONObject result = new JSONObject(map);
                            MQTTController.sendDebugInfo(contentType, result);
                        }
                    }
                    //检查物关节点
                    List<ItemManager>itemManagers = ItemManagerController.MQTTgetAllItemManagerNode();
                    for(int i = 0; i < itemManagers.size(); ++i){
                        ItemManager itemManager = itemManagers.get(i);
                        if(itemManager.getName().equals(nodeName)){
                            //find the itemManager
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("物关",itemManager);
                            JSONObject result = new JSONObject(map);
                            MQTTController.sendDebugInfo(contentType, result);
                        }
                        else{
                            //判断是否是该子物关的下一层
                            if(nodeName.contains(itemManager.getName())){
                                //名称包含，说明在该子物关的下一层,对该子物关进行查询
                                String sourceManager = "调试工具";
                                String sourceName = "调试工具";
                                String targetManager = itemManager.getName();
                                String targetName = nodeName;
                                String targetData = "节点搜索";
                                Map<String, Object> jdata = new HashMap<String, Object>();
                                jdata.put("目标地址", targetManager);
                                jdata.put("目标名称", targetName);
                                jdata.put("发送地址", sourceManager);
                                jdata.put("发送名称", sourceName);
                                jdata.put("请求数据", targetData);
                                Map<String, Object> messageMap = new HashMap<String, Object>();
                                messageMap.put("消息内容", jdata);
                                messageMap.put("消息类型", contentType);
                                messageMap.put("coding standard","utf8");
                                JSONObject sendJson = new JSONObject(messageMap);
                                MQTTController.sendDebugInfo(contentType, sendJson);
                            }
                        }
                    }
                    //地址在本节点下，但是数据库不存在内容，所以返回空
                    MQTTController.sendDebugInfo(contentType, null);
                }
                else{
                    //不是本物关下的节点，对上层进行搜索
                    String sourceManager = "调试工具";
                    String sourceName = "调试工具";
                    String targetManager = Constants.parentAddress;
                    String targetName = nodeName;
                    String targetData = "节点搜索";
                    Map<String, Object> jdata = new HashMap<String, Object>();
                    jdata.put("目标地址", targetManager);
                    jdata.put("目标名称", targetName);
                    jdata.put("发送地址", sourceManager);
                    jdata.put("发送名称", sourceName);
                    jdata.put("请求数据", targetData);
                    Map<String, Object> messageMap = new HashMap<String, Object>();
                    messageMap.put("消息内容", jdata);
                    messageMap.put("消息类型", contentType);
                    messageMap.put("coding standard","utf8");
                    JSONObject sendJson = new JSONObject(messageMap);
                    MQTTController.sendDebugInfo(contentType, sendJson);
                }
            }
        }
    }
}