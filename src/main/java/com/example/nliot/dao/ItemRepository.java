package com.example.nliot.dao;

import java.util.List;

import com.example.nliot.entity.Item;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends GraphRepository<Item> {
    @Query("create (n:物设备{物名:{name},上级物关名:{managerName},物功能表:{url},物类型:{type}})")
    Void addItemNode(@Param("name") String name, @Param("managerName")String address, @Param("url")String ip, @Param("type")String type);

    @Query("MATCH (n:物设备) RETURN n ")
    List<Item> getItemNodeList();

    @Query("MATCH (n:物设备) Where n.物名 = {name} RETURN n ")
    Item getItemNodeByName(@Param("name") String name);

    @Query("Match (n:物设备) Where n.物名 = {name} Delete n ")
    Void deleteItemNode(@Param("name") String name);
}
