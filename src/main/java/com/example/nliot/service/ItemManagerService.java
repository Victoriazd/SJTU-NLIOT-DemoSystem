package com.example.nliot.service;

import java.util.List;

import com.example.nliot.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nliot.entity.ItemManager;
import com.example.nliot.dao.ItemManagerRepository;

@Service

public class ItemManagerService {
    private final ItemManagerRepository itemManagerRepository;

    @Autowired
    public ItemManagerService(ItemManagerRepository itemManagerRepository) {
        this.itemManagerRepository = itemManagerRepository;
    }

    public  List<ItemManager> getItemManagerNodeList() {
        return itemManagerRepository.getItemNodeList();
    }

    public void addItemManagerNode(ItemManager item){
        itemManagerRepository.addItemNode(item.getName(),item.getAddress(),item.getIp());
    }


    public void deleteItemManagerNode(String name){
        itemManagerRepository.deleteItemNode(name);
    }

    public ItemManager getItemManagerNodeByName(String name){
        return itemManagerRepository.getItemNodeByName(name);
    }

    public List<ItemManager> getChildItemManagerNodeByName(String name){
        return itemManagerRepository.getChildItemManagerNodeByName(name);
    }

    public List<Item> getChildItemNodeByName(String name){
        return itemManagerRepository.getChildItemNodeByName(name);
    }


}
