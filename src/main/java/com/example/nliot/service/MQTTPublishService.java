package com.example.nliot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.nliot.MQTT.ClientMQTT;
import com.example.nliot.MQTT.ServerMQTT;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



@Service
public class MQTTPublishService {
    public void sendMessage (String topic, JSONObject message) throws UnsupportedEncodingException, MqttException, InterruptedException {
        String MQTTObject = JSON.toJSONString(message);
        System.out.println(MQTTObject);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);
        mqttMessage.setPayload(MQTTObject.getBytes("UTF-8"));

        ServerMQTT serverMQTT = new ServerMQTT(topic);
        serverMQTT.publish(mqttMessage);
        System.out.println(mqttMessage.isRetained() + "------ratained状态");
    }

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
        MQTTPublishService mqttService = new MQTTPublishService();

        Map<String, Object> data1 = new HashMap<String, Object>();
        data1.put("111","111");
        JSONObject json1 = new JSONObject(data1);

        mqttService.sendMessage("3503",json1);

    }
}