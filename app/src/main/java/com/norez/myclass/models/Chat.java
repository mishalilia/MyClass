package com.norez.myclass.models;

public class Chat {
    String name, last_message, id;

    public Chat(String name, String last_message) {
        this.name = name;
        this.last_message = last_message;
    }

    public String getLast_message() {
        return last_message;
    }

    public String getName() {
        return name;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
