package com.example.postgres;

import com.example.postgres.models.Approvaltimesheet;
import com.example.postgres.repositories.ApprovalRepo;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Approvaltimesheet_MicroService {

	public static void main(String[] args) {
		SpringApplication.run(Approvaltimesheet_MicroService.class, args);
	}
    @Bean
	public CommandLineRunner commandLineRunner(ApprovalRepo repository) {
		return args -> {
			for(int i=0; i<50;i++){
				Faker faker=new Faker();
				var sheet= Approvaltimesheet.builder()
						.memberId((long) faker.number().numberBetween(1,50))
						.build();
				repository.save(sheet);
			}

			};

	}
}
