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
    @Query("create (n:物设备{物设备名:{name},物设备地址:{address},ip:{ip}})")
    Void addItemNode(@Param("name") String name, @Param("address")String address, @Param("ip")String ip);

    @Query("MATCH (n:物设备) RETURN n ")
    List<Item> getItemNodeList();

    @Query("MATCH (n:物设备) Where n.物设备名 = {name} RETURN n ")
    Item getItemNodeByName(@Param("name") String name);

    @Query("Match (n:物设备) Where n.物设备名 = {name} Delete n ")
    Void deleteItemNode(@Param("name") String name);
}
