package com.example.nliot.entity;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;


@NodeEntity(label = "物设备")
public class Item {

    @GraphId
    @Property
    private  Long id;

    @Property(name = "物名")
    private String name;
    @Property(name = "物类型")
    private String type;
    @Property(name = "物功能表")
    private String url;
    @Property(name = "上级物关名")
    private String managerName;
    @Property(name = "注册时间")
    private String createTime;
    @Property(name = "修改时间")
    private String modifyTime;
    @Property(name = "所有人")
    private String user;

    public Item() {
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getCreateTime(){
        return createTime;
    }

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }

    public String getModifyTime(){
        return modifyTime;
    }

    public void setModifyTime(String modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }
}
