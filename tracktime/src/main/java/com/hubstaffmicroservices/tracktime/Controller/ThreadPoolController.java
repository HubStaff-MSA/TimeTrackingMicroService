package com.hubstaffmicroservices.tracktime.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadPoolController {

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ThreadPoolController(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @PutMapping("/thread-pool/max-thread-count")
    public ResponseEntity<String> setMaxThreadCount(@RequestParam Integer maxThreadCount) {
        try {
            taskExecutor.setCorePoolSize(maxThreadCount);
            taskExecutor.setMaxPoolSize(maxThreadCount);
            taskExecutor.setQueueCapacity(maxThreadCount * 2);
            return ResponseEntity.ok("Max thread count updated successfully"+taskExecutor.getMaxPoolSize());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating max thread count: " + e.getMessage());
        }
    }
//    @PutMapping("/set-max-db-connections-count")
//    public ResponseEntity<String> setMaxDbConnectionsCount(@RequestParam int maxDbConnectionsCount) {
//        try {
//            if (maxDbConnectionsCount <= 0) {
//                return ResponseEntity.badRequest().body("Max DB connections count must be greater than zero");
//            }
//
//            if (dataSource instanceof HikariDataSource) {
//                HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
//                hikariDataSource.setMaximumPoolSize(maxDbConnectionsCount);
//                return ResponseEntity.ok("Max DB connections count updated successfully");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Failed to update max DB connections count: DataSource is not of type HikariDataSource");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to update max DB connections count: " + e.getMessage());
//        }
//    }
}
