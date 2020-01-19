package com.example.nliot.MQTT;

import java.util.concurrent.ScheduledExecutorService;

import com.example.nliot.Constants;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 模拟一个客户端接收消息
 * @author rao
 *
 */
public class LiveClientMQTT {
    public static final String HOST = Constants.mqttUrl;
    public static final String TOPIC1 = "pos_message_all";
    private static final String clientid = Constants.physicalAddress;
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "admin";    //非必须
    private String passWord = "password";  //非必须
    @SuppressWarnings("unused")
    private ScheduledExecutorService scheduler;

    public void start(String topic){
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调
            client.setCallback(new LiveClientCallBack());

            MqttTopic topic2 = client.getTopic(TOPIC1);
//      setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//      options.setWill(topic, "close".getBytes(), 2, true);  //遗嘱
            client.connect(options);
            //订阅消息
            int[] Qos  = {1};
            String[] topic1 = {topic};
            client.subscribe(topic1, Qos);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}