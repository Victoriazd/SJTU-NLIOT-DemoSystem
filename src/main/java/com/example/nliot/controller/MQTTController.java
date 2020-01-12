package com.example.nliot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.entity.Item;
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

    private final MQTTPublishService mqttPublishService;

    @Autowired
    public MQTTController(MQTTPublishService mqttPublishService) {
        this.mqttPublishService = mqttPublishService;
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








}
