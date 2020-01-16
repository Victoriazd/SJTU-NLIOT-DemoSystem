package com.example.nliot.controller;

import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ThingController {
    private static final Logger logger = LoggerFactory.getLogger(ThingController.class);

    @Autowired
    WebsocketEndpoint websocketEndpoint;
}
