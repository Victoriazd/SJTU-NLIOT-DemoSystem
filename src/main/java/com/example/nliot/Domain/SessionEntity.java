package com.example.nliot.Domain;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.Future;

public class SessionEntity {
    private Session session;
    private String id;

    public SessionEntity(Session se, String id){
        this.session = se;
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Future sendAsync(String msg){
        return session.getAsyncRemote().sendText(msg);
    }

    public void sendSync(String msg){
        try{
            session.getBasicRemote().sendText(msg);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
