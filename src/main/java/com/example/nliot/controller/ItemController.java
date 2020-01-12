package com.example.nliot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.nliot.entity.Item;
import com.example.nliot.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ItemController {

    private final ItemService testService;

    @Autowired
    public ItemController(ItemService testService) {
        this.testService = testService;
    }

    @RequestMapping(path = "/getAllItemNode", method = RequestMethod.GET)
    public String getAllItemNode() {
        List<Item> ret = testService.getItemNodeList();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result",ret);
        JSONObject json = new JSONObject(map);
        String res = json.toString();
        return res;
    }

    @RequestMapping(path = "/addItemNode", method = RequestMethod.GET)
    public String addUserNode() {
        Item item = new Item();
        item.setName("testName");
        item.setAddress("testAddress");
        item.setIp("testIp");
        testService.addItemNode(item);
        return "ok";
    }

    @RequestMapping(path = "/deleteItemNode", method = RequestMethod.GET)
    public String deleteUserNode() {
        testService.deleteItemNode("testName");
        return "ok";
    }

    @RequestMapping(path = "/getItemNodeByName/{name}", method = RequestMethod.GET)
    public Item getUserNode(@PathVariable String name) {
        Item item = testService.getItemNodeByName(name);
        return item;
    }








}
