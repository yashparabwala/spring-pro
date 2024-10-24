package com.bezkoder.spring.files.upload.db.model;


public class FileResponse {
    private String id;
    private String name;
    private String type;
    private String groupName;

    public FileResponse(String id, String name, String type, String groupName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.groupName = groupName;
    }

    public String getFileName() {
        return name;
    }
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

