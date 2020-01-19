package com.example.nliot.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


@NodeEntity(label = "物关")
public class ItemManager {
    @GraphId
    @Property
    private  Long id;

    @Property(name = "物关名")
    private String name;
    @Property(name = "别名")
    private String anothername;
    @Property(name = "通信地址")
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

    public String getAnothername() {
        return anothername;
    }

    public void setAnothername(String name) {
        this.anothername = anothername;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
