package com.hubstaffmicroservices.tracktime;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class TracktimeApplication {

	BigController bigController = new BigController();
//	TrackTimeScheduled trackTimeScheduled = new TrackTimeScheduled();
    public static void main(String[] args) {
		SpringApplication.run(TracktimeApplication.class, args);

	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			@Override
			public void run(ApplicationArguments args) throws Exception
			{
//				HikariDataSource dataSource = databaseConfig.createDataSource();
//				HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();
//				System.out.println("Active Connections: " + poolMXBean.getActiveConnections());
//				System.out.println("Idle Connections: " + poolMXBean.getIdleConnections());
//				System.out.println("Total Connections: " + poolMXBean.getTotalConnections());
				Class<?> newCommand = Class.forName("com.hubstaffmicroservices.tracktime.UpdatedClass");
//				trackTimeScheduled.transferDataFromCacheToDatabase();
//				System.out.println(newCommand);
//				System.out.println(bigController.TestModify());
//				System.out.println(bigController.replace("com.hubstaffmicroservices.tracktime.UpdatedClass", "add", bigController.TestModify()));

			}
		};
	}
}
