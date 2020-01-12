package com.example.nliot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nliot.entity.Item;
import com.example.nliot.dao.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public  List<Item> getItemNodeList() {
        return itemRepository.getItemNodeList();
    }

    public void addItemNode(Item item){
        itemRepository.addItemNode(item.getName(),item.getAddress(),item.getIp());
    }


    public void deleteItemNode(String name){
        itemRepository.deleteItemNode(name);
    }

    public Item getItemNodeByName(String name){
        return itemRepository.getItemNodeByName(name);
    }
}