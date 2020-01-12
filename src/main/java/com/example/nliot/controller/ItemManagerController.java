package com.example.nliot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.entity.Item;
import com.example.nliot.entity.ItemManager;
import com.example.nliot.service.ItemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ItemManagerController {
    private final ItemManagerService testManagerService;

    @Autowired
    public ItemManagerController(ItemManagerService testManagerService) {
        this.testManagerService = testManagerService;
    }

    @RequestMapping(path = "/getAllItemManagerNode", method = RequestMethod.GET)
    public List<ItemManager> getAllItemManagerNode() {
        List<ItemManager> ret = testManagerService.getItemManagerNodeList();
        return ret;
    }

    @RequestMapping(path = "/addItemManagerNode", method = RequestMethod.GET)
    public String addItemManagerNode() {
        ItemManager itemManager = new ItemManager();
        itemManager.setName("testName");
        itemManager.setAddress("testAddress");
        itemManager.setIp("testIp");
        testManagerService.addItemManagerNode(itemManager);
        return "ok";
    }

    @RequestMapping(path = "/deleteItemManagerNode/{name}", method = RequestMethod.GET)
    public String deleteItemManagerNode(@PathVariable String name) {
        testManagerService.deleteItemManagerNode(name);
        return "ok";
    }

    @RequestMapping(path = "/getItemManagerNodeByName/{name}", method = RequestMethod.GET)
    public String SearchNodeList(@PathVariable String name) {
        List<ItemManager> itemManagers = testManagerService.getChildItemManagerNodeByName(name);
        List<Item> items = testManagerService.getChildItemNodeByName(name);
        Map<String, Object> map = new HashMap<String, Object>();
        if (itemManagers.size() == 0){
            ItemManager 空 = new ItemManager();
            空.setName("无搜索结果");
            itemManagers.add(空);
        }
        if (items.size() == 0){
            Item 空 = new Item();
            空.setName("无搜索结果");
            items.add(空);
        }
        map.put("itemManagers",itemManagers);
        map.put("items",items);
        JSONObject json = new JSONObject(map);
        String res = json.toString();
        return res;
    }
}
