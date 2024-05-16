package com.hubstaffmicroservices.tracktime.MQ;

import java.io.Serializable;

public class ProjectDTO implements Serializable {

    private int id;
    private String projectName;

    public ProjectDTO(int id, String projectName) {
        this.id = id;
        this.projectName = projectName;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}