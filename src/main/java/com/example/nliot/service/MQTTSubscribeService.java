package com.example.nliot.service;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.MQTT.ClientMQTT;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.UnsupportedEncodingException;

public class MQTTSubscribeService {
    public String  getMessage(String topic) throws InterruptedException {
        ClientMQTT client = new ClientMQTT();
        client.start(topic);
        String message = "111";
        while (true){
            if (message !=client.resc()){
                message = client.resc();
                return message;
            }
            Thread.currentThread().sleep(1000);
        }
    }
    public static void main(String[] args) throws MqttException, UnsupportedEncodingException, InterruptedException {
        MQTTSubscribeService mqttSubscribeService = new MQTTSubscribeService();
        MQTTPublishService mqttPublishService = new MQTTPublishService();
        String test = mqttSubscribeService.getMessage("3503");
        JSONObject json = JSONObject.parseObject(test);
        String contentType = json.getString("消息类型");
        String dataStr = json.getString("消息内容");
        JSONObject data = JSONObject.parseObject(dataStr);
        if (contentType == "请求连接"){
            String targetAddress = data.getString("目标地址");
            mqttPublishService.sendMessage(targetAddress,json);
        }
        if (contentType == "反馈信息"){

        }

    }
}
