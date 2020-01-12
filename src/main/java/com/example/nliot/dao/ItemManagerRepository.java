package com.example.nliot.dao;

import java.util.List;

import com.example.nliot.entity.Item;
import com.example.nliot.entity.ItemManager;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemManagerRepository extends GraphRepository<ItemManager> {
    @Query("create (n:物管理器{物管理器别名:{name},物管理器地址:{address},ip:{ip}}) RETURN n ")
    Void addItemNode(@Param("name") String name, @Param("address")String address, @Param("ip")String ip);

    @Query("MATCH (n:物管理器) RETURN n ")
    List<ItemManager> getItemNodeList();

    @Query("MATCH (n:物管理器) Where n.物管理器别名 = {name} RETURN n ")
    ItemManager getItemNodeByName(@Param("name") String name);

    @Query("MATCH (n:物管理器)-[r:上级]->(m:物管理器) Where m.物管理器别名 = {name} RETURN n ")
    List<ItemManager> getChildItemManagerNodeByName(@Param("name") String name);

    @Query("MATCH (n:物设备)-[r:部署]->(m:物管理器) Where m.物管理器别名 = {name} RETURN n ")
    List<Item> getChildItemNodeByName(@Param("name") String name);

    @Query("Match (n:物管理器) Where n.物管理器别名 = {name} Delete n ")
    Void deleteItemNode(@Param("name") String name);



}
