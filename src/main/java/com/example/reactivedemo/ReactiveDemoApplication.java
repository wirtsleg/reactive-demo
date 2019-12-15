package com.example.reactivedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class ReactiveDemoApplication {

    public static void main(String[] args) {
        ReactorDebugAgent.init();
        SpringApplication.run(ReactiveDemoApplication.class, args);
    }
}
