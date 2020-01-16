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

    @Query("MATCH (n:物关)-[r:上级]->(m:物关) Where m.物关名 = {name} or n.别名 = {name} RETURN n ")
    List<ItemManager> getChildItemManagerNodeByName(@Param("name") String name);

    @Query("MATCH (t:物设备)-[r:挂载]->(n:物关) Where n.物关名 = {name} or n.别名 = {name} RETURN t ")
    List<Item> getChildItemNodeByName(@Param("name") String name);

    @Query("Match (n:物管理器) Where n.物管理器别名 = {name} Delete n ")
    Void deleteItemNode(@Param("name") String name);



}
