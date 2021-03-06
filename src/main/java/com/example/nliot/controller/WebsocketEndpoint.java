package com.example.nliot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.nliot.Constants;
import com.example.nliot.Domain.SessionEntity;
import com.example.nliot.entity.Item;
import com.example.nliot.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{id}")
@Component
public class WebsocketEndpoint {

    public static final Map<String, SessionEntity> sessionMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebsocketEndpoint.class);

    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        if (sessionMap.containsKey(id)){
            String warning = String.format("Server Connection of %s has been established", id);
            logger.info(warning);
            session.getAsyncRemote().sendText(warning);
        }
        logger.info(String.format("Server Connection of %s is established", id));
        SessionEntity se = new SessionEntity(session, id);
        sessionMap.put(id, se);
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String id) {
        logger.info(String.format("Server Connection of %s is closed", id));
        sessionMap.remove(id);
        //连接关闭，在数据表中进行删除
        ItemService service = applicationContext.getBean(ItemService.class);
        service.deleteItemNode(sessionMap.get(id).getDeviceName());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("id") String id) {
        logger.info(String.format("Server Get Message From %s: " + message, id));
        //收到注册（登录请求），在数据表中进行添加
        JSONObject jsonObject = JSON.parseObject(message);
        String Addr = jsonObject.getString("物功能表");
        String Name = jsonObject.getString("物名");
        String Type = jsonObject.getString("物类型");
        String User = jsonObject.getString("所有人");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        String CreateTime = str;
        String ModifyTime = str;
        sessionMap.get(id).setDeviceName(Name);
        if(Addr != null && Name != null && Type != null && (!Addr.equals("")) && (!Name.equals("")) && (!Type.equals(""))){
            //符合规定的信息
            Item item = new Item();
            item.setName(Name);
            item.setManagerName(Constants.physicalAddress);
            item.setUrl(Addr);
            item.setType(Type);
            item.setUser(User);
            item.setCreateTime(CreateTime);
            item.setModifyTime(ModifyTime);
            ItemService service = applicationContext.getBean(ItemService.class);
            service.deleteItemNode(Name);
            service.addItemNode(item);
        }
        else {
            logger.info(String.format("Server Connection of %s is closed <illegal access>", id));
            sessionMap.remove(id);
        }
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("id") String id) {
        error.printStackTrace();
        session.getAsyncRemote().sendText(error.getMessage());
    }
}
