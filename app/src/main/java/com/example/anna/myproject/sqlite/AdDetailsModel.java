package com.example.anna.myproject.sqlite;

/**
 * Created by Anna on 30-10-2015.
 */
public class AdDetailsModel {

     int id;
     String title;
     String category;
     String description;
     String name;
     String phone;
     String location;
     int price;
     byte[] image;

    public AdDetailsModel() {
    }

    public AdDetailsModel(String title, String category, String description, String name, String phone, String location, int price, byte[] image) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
