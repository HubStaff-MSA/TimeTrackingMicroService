//package com.hubstaffmicroservices.tracktime.Controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TaskController {
//
//    @Autowired
////    private ThreadPoolTaskExecutor taskExecutor;
//
//    @GetMapping("/execute-task")
//    public String executeTask() {
//        taskExecutor.execute(() -> {
//            // Task logic here
//            System.out.println("Task executed by thread: " + Thread.currentThread().getName());
//        });
//        //System.out.println(taskExecutor.getMaxPoolSize());
//        return "Task execution initiated";
//    }
//}
