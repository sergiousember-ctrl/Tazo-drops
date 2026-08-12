package com.tazos.plugin.items;

public class Tazo {

    private final int id;
    private final String name;
    private final String type;
    private final String imageUrl;

    public Tazo(int id, String name, String type, String imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
