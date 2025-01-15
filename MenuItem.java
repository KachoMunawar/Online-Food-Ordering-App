package com.example.fooddelivery.Customer;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private String description;
    private boolean isAvailable;
    private int quantity;

    public MenuItem() {}

    public MenuItem(String id, String name, double price, String description, int quantity,String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }
    public MenuItem(String id, String name, double price, String description, int quantity, String imageUrl, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }
    public MenuItem(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
