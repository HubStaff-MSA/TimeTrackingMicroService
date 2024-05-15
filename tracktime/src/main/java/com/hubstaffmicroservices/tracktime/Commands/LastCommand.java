package com.hubstaffmicroservices.tracktime.Commands;


import org.springframework.cloud.context.config.annotation.RefreshScope;

public class LastCommand {
    public static int add(int a, int b) {
        return a / b;
    }
}
