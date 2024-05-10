package com.hubstaffmicroservices.tracktime.Controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class FreezeConfig {

    private boolean isFrozen = false;

    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public Runnable freezeApp() {
        return () -> {
            // Check isFrozen flag and freeze if true
            if (isFrozen) {
                freeze();
            }
        };
    }

    // Setter method for isFrozen flag
    public synchronized void setIsFrozen(boolean isFrozen) {
        // If isFrozen is set to true, freeze the application
        if (isFrozen) {
            log.info("Freezing application...");
            freeze();
        }
        // If isFrozen is set to false, unfreeze the application
        else {
            log.info("Unfreezing application...");
            unfreeze();
        }
    }

    private synchronized void freeze() {
        isFrozen = true;
        dataSource.close();
    }

    private synchronized void unfreeze() {
        isFrozen = false;
        dataSource = createDataSource(); // Reinitialize the datasource
    }

    @PreDestroy
    public synchronized void releaseResources() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5433/abdallahehab");
        config.setMaximumPoolSize(10);  // Adjust the number as needed
        config.setMinimumIdle(5);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(1800000);
        config.setRegisterMbeans(true); // This enables JMX metrics
        config.setPoolName("MyHikariPool");
        return new HikariDataSource(config);
    }
}
