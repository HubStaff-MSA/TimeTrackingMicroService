package com.hubstaffmicroservices.tracktime.Controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

//@Configuration
//@Data
public class DatabaseConfig {

    private int set_max_db_connections_count = 4;
    private HikariDataSource dataSource; // Store a reference to the data source

//    @Bean
    public HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        // Database connection settings
        config.setJdbcUrl("jdbc:postgresql://localhost:5433/abdallahehab");
//        config.setUsername("");
//        config.setPassword("");

        // Set the maximum number of connections
        config.setMaximumPoolSize(set_max_db_connections_count);  // Adjust the number as needed

        // Optional: Additional settings
        config.setMinimumIdle(5);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(1800000);

        // Enable HikariCP metrics

        // Set a custom pool name to avoid conflicts
        config.setPoolName("MyHikariPool");

        // Create a HikariDataSource instance with the configuration
        return new HikariDataSource(config);
    }

//    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    // Method to update the maximum connection count
    public void updateMaxDbConnectionsCount(int newMaxCount) {
        dataSource = createDataSource();
        set_max_db_connections_count = newMaxCount;
        dataSource.setMaximumPoolSize(set_max_db_connections_count); // Update the data source
        printConnectionPoolMetrics(); // Print the connection pool metrics
    }

    // Helper method to print connection pool metrics
    private void printConnectionPoolMetrics() {
        System.out.println("Active Connections: " + dataSource.getHikariPoolMXBean().getActiveConnections());
        System.out.println("Idle Connections: " + dataSource.getHikariPoolMXBean().getIdleConnections());
        System.out.println("Total Connections: " + dataSource.getHikariPoolMXBean().getTotalConnections());
    }
}
