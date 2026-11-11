package com.example.quanlikhohang.model;

public class Product {

    private int id;
    private String name;
    private int price;
    private int quantity;
    private String category;
    private int active;
    private String description;

    // Constructor
    public Product(int id, String name, int price,
                   int quantity, String category,
                   int active, String description) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.description = description;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public int getActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }
}
