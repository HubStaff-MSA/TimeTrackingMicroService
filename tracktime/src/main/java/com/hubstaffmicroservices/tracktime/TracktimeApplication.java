package com.hubstaffmicroservices.tracktime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TracktimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TracktimeApplication.class, args);
	}

}
