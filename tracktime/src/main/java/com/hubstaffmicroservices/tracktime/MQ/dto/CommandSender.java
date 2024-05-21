package com.hubstaffmicroservices.tracktime.MQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandSender implements Serializable {

    private String command;
    private Object payload;
    private String requestQueue;

    // Default constructor
    // Parameterized constructor
}
