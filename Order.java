package com.example.fooddelivery.Customer;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private List<MenuItem> items;
    private String address;
    private String status;
    private double totalPrice;

    public Order() {}

    public Order(String orderId, String userId, List<MenuItem> items, String address, String status, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.address = address;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
