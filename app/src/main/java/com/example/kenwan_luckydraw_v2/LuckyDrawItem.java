package com.example.kenwan_luckydraw_v2;

public class LuckyDrawItem {
    private int id;
    private String name;
    private String details;
    private int weight;

    public LuckyDrawItem() {}

    public LuckyDrawItem(int id, String name, String details, int weight) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {return details;}

    public void setDetails(String details) {
        this.details = details;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
