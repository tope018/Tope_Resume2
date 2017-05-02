package com.example.tope0_000.tope_resume2;

public class Skill {

    private int id;
    private String name;
    private String description;

    public Skill() {}

    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Skill(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    //Setter methods
    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    //Getter methods
    public int getId() { return id; }

    public String getName() { return name; };

    public String getDescription() { return description; };
}
