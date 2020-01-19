package com.example.nliot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.Constants;
import com.example.nliot.MQTT.ClientMQTT;
import com.example.nliot.MQTT.LiveClientMQTT;
import com.example.nliot.entity.Item;
import com.example.nliot.entity.ItemManager;
import com.example.nliot.service.ItemService;
import com.example.nliot.service.MQTTPublishService;
import com.example.nliot.service.MQTTSubscribeService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class MQTTController {

    private static MQTTPublishService mqttPublishService;

    LiveClientMQTT clientMQTT = new LiveClientMQTT();

    @Autowired
    public MQTTController(MQTTPublishService mqttPublishService) {

        this.mqttPublishService = mqttPublishService;
        clientMQTT.start(Constants.physicalAddress);
    }


    @RequestMapping(path = "/getFunctionTableByTool/{ItemManagerName}/{ItemName}", method = RequestMethod.GET)
    public String getUserNode(@PathVariable String ItemManagerName, @PathVariable String ItemName) throws InterruptedException, UnsupportedEncodingException, MqttException {

        String sourceManager = "调试工具地址";
        String sourceName = "调试工具";
        String targetManager = ItemManagerName;
        String targetName = ItemName;
        String targetData = "物功能表";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("目标地址", targetManager);
        data.put("目标名称", targetName);
        data.put("发送地址", sourceManager);
        data.put("发送名称", sourceName);
        data.put("请求数据", targetData);
        Map<String, Object> messageMap = new HashMap<String, Object>();
        String contentType = "请求连接";
        messageMap.put("消息内容", data);
        messageMap.put("消息类型", contentType);
        messageMap.put("coding standard","utf8");
        JSONObject sendJson = new JSONObject(messageMap);
        mqttPublishService.sendMessage(ItemManagerName, sendJson);

        String test = mqttPublishService.getMessage("调试工具地址");
        JSONObject getJson = JSONObject.parseObject(test);
        JSONObject messageContent = getJson.getJSONObject("消息内容");
        String functionTable = messageContent.getString("数据接口");

        return functionTable;
    }

    //请求获取某一物关下的全部节点信息（包括全部的物和物关）
    @RequestMapping(path = "/getNode/{ItemManagerName}", method = RequestMethod.GET)
    public String getManagerNode(@PathVariable String ItemManagerName) throws InterruptedException, UnsupportedEncodingException, MqttException {
        String sourceManager = "调试工具";
        String sourceName = "调试工具";
        String targetManager = ItemManagerName;
        String targetName = ItemManagerName;
        String targetData = "节点列表";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("目标地址", targetManager);
        data.put("目标名称", targetName);
        data.put("发送地址", sourceManager);
        data.put("发送名称", sourceName);
        data.put("请求数据", targetData);
        Map<String, Object> messageMap = new HashMap<String, Object>();
        String contentType = "节点列表";
        messageMap.put("消息内容", data);
        messageMap.put("消息类型", contentType);
        messageMap.put("coding standard","utf8");
        JSONObject sendJson = new JSONObject(messageMap);
        mqttPublishService.sendMessage(ItemManagerName, sendJson);
        String result = mqttPublishService.getMessage("节点列表");
        mqttPublishService.sendMessage("节点列表", null);
        //获取一次后置空
        return result;
    }

    //请求获取某一物关下的某个节点信息
    @RequestMapping(path = "/getNode/{ItemManagerName}/{nodeName}", method = RequestMethod.GET)
    public String getNodeByName(@PathVariable String ItemManagerName, @PathVariable String nodeName) throws InterruptedException, UnsupportedEncodingException, MqttException {
        String sourceManager = "调试工具";
        String sourceName = "调试工具";
        String targetManager = ItemManagerName;
        String targetName = nodeName;
        String targetData = "节点查询";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("目标地址", targetManager);
        data.put("目标名称", targetName);
        data.put("发送地址", sourceManager);
        data.put("发送名称", sourceName);
        data.put("请求数据", targetData);
        Map<String, Object> messageMap = new HashMap<String, Object>();
        String contentType = "节点查询";
        messageMap.put("消息内容", data);
        messageMap.put("消息类型", contentType);
        messageMap.put("coding standard","utf8");
        JSONObject sendJson = new JSONObject(messageMap);
        mqttPublishService.sendMessage(ItemManagerName, sendJson);
        String result = mqttPublishService.getMessage("节点查询");
        mqttPublishService.sendMessage("节点查询", null);
        //获取一次后置空
        return result;
    }

    //查询搜索某一个物设备或者物节点
    @RequestMapping(path = "/searchNode/{nodeName}", method = RequestMethod.GET)
    public String searchNodeByName(@PathVariable String nodeName) throws InterruptedException, UnsupportedEncodingException, MqttException {

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
                    return result.toJSONString();
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
                    return result.toJSONString();
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
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("目标地址", targetManager);
                        data.put("目标名称", targetName);
                        data.put("发送地址", sourceManager);
                        data.put("发送名称", sourceName);
                        data.put("请求数据", targetData);
                        Map<String, Object> messageMap = new HashMap<String, Object>();
                        String contentType = "节点搜索";
                        messageMap.put("消息内容", data);
                        messageMap.put("消息类型", contentType);
                        messageMap.put("coding standard","utf8");
                        JSONObject sendJson = new JSONObject(messageMap);
                        mqttPublishService.sendMessage(targetManager, sendJson);
                        String result = mqttPublishService.getMessage("节点搜索");
                        mqttPublishService.sendMessage("节点搜索", null);
                        return result;
                    }
                }
            }
            //地址在本节点下，但是数据库不存在内容，所以返回空
            return "";
        }
        else{
            //不是本物关下的节点，对上层进行搜索
            String sourceManager = "调试工具";
            String sourceName = "调试工具";
            String targetManager = Constants.parentAddress;
            String targetName = nodeName;
            String targetData = "节点搜索";
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("目标地址", targetManager);
            data.put("目标名称", targetName);
            data.put("发送地址", sourceManager);
            data.put("发送名称", sourceName);
            data.put("请求数据", targetData);
            Map<String, Object> messageMap = new HashMap<String, Object>();
            String contentType = "节点搜索";
            messageMap.put("消息内容", data);
            messageMap.put("消息类型", contentType);
            messageMap.put("coding standard","utf8");
            JSONObject sendJson = new JSONObject(messageMap);
            mqttPublishService.sendMessage(targetManager, sendJson);
            String result = mqttPublishService.getMessage("节点搜索");
            mqttPublishService.sendMessage("节点搜索", null);
            //获取一次后置空
            return result;
        }
    }

    //functions, 该物关发送一个mqtt消息，topic + content
    public static void sendDebugInfo(String topic, JSONObject content) throws InterruptedException, UnsupportedEncodingException, MqttException {
        mqttPublishService.sendMessage(topic, content);
    }









}
