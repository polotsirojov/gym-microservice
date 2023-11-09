package com.example.gymreportserver.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver{

    @JmsListener(destination = "gym.report", containerFactory = "myFactory")
    public void receiveMessage(String request) {
        System.out.println("Received <" + request + ">");
    }
}
