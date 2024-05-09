package com.example.postgres;

import com.example.postgres.models.Timesheets;
import com.example.postgres.repositories.TimesheetRepo;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class  PostgresApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgresApplication.class, args);
	}
    @Bean
	public CommandLineRunner commandLineRunner(TimesheetRepo repository) {
		return args -> {
			for(int i=0; i<50;i++){
				Faker faker=new Faker();
				var sheet= Timesheets.builder()
						.memberId((long) faker.number().numberBetween(1,50))
						.build();
				repository.save(sheet);
			}

			};

	}
}
