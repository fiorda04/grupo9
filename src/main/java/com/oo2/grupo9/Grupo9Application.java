package com.oo2.grupo9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Grupo9Application {

	public static void main(String[] args) {
		SpringApplication.run(Grupo9Application.class, args);
	}

}
