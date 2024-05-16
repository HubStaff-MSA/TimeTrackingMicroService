package com.hubstaffmicroservices.tracktime.MQ;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RabbitMQListener {

    public void receiveMessage(List<?> projectDTOs) {
        List<ProjectDTO> projects = projectDTOs.stream()
                .map(item -> {
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) item;
                    return new ProjectDTO(Integer.valueOf(map.get("id").toString()), map.get("projectName").toString());
                })
                .collect(Collectors.toList());

        System.out.println("Received list of projects: " + projects);
        projects.forEach(project ->
                System.out.println("Project ID: " + project.getId() + ", Project Name: " + project.getProjectName()));
    }
}
