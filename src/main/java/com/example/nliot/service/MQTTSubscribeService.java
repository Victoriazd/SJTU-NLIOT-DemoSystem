package com.example.nliot.service;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.MQTT.ClientMQTT;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.UnsupportedEncodingException;

public class MQTTSubscribeService {
    int timeout = 5;

    public String  getMessage(String topic) throws InterruptedException {
        ClientMQTT client = new ClientMQTT();
        client.start(topic);
        String message;
        int count = 0;
        while (count < timeout){
            if (client.resc() != null){
                message = client.resc();
                client.reinitResc();
                System.out.println("print message: " + message);
                return message;
            }
            count += 1;
            Thread.sleep(1000);
        }
        return "";
    }
    public static void main(String[] args) throws MqttException, UnsupportedEncodingException, InterruptedException {
        MQTTSubscribeService mqttSubscribeService = new MQTTSubscribeService();
        MQTTPublishService mqttPublishService = new MQTTPublishService();
        String test = mqttSubscribeService.getMessage("3503");
        JSONObject json = JSONObject.parseObject(test);
        String contentType = json.getString("消息类型");
        String dataStr = json.getString("消息内容");
        JSONObject data = JSONObject.parseObject(dataStr);
        if(contentType != null){
            if (contentType.equals("请求连接")){
                String targetAddress = data.getString("目标地址");
                System.out.println("debug: code get here??????-----");
                mqttPublishService.sendMessage(targetAddress,json);
            }
            if (contentType.equals("反馈信息")){

            }
        }
    }
}
