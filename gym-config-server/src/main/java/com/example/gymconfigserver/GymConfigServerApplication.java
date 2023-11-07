package com.example.gymconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class GymConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymConfigServerApplication.class, args);
	}

}
