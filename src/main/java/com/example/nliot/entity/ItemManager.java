package com.example.nliot.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


@NodeEntity(label = "物管理器")
public class ItemManager {
    @GraphId
    @Property
    private  Long id;

    @Property(name = "物管理器别名")
    private String name;
    @Property(name = "物管理器地址")
    private String address;
    @Property(name = "ip")
    private String ip;

    public ItemManager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
