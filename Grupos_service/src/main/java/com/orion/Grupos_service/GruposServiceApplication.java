package com.orion.Grupos_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class GruposServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GruposServiceApplication.class, args);
	}

}
