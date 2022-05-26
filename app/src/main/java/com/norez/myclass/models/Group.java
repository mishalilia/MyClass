package com.norez.myclass.models;

import java.util.ArrayList;

public class Group {
    private String name, organizer;
    private ArrayList<String> members;
    public static Group current_group;

    public Group(String name, String organizer, ArrayList<String> members) {
        this.name = name;
        this.organizer = organizer;
        this.members = members;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}