package com.project.restaurantsbenchmark.model;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;
    private String city ;
    private String timeOpen;

    private String timeClose ;

    private String status ;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        Description = description;
    }

    private String Description;
    private String Details;

    private String Delivery;

    public void setDelivery(String delivery) {
        Delivery = delivery;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDetails() {
        return Details;
    }

    public String getDelivery() {
        return Delivery;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return Description;
    }

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images;
    // Constructors, getters, and setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public Restaurant() {
        this.ratings = new ArrayList<>();
    }

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
        this.ratings = new ArrayList<>();
    }

    // Getters and setters

    // ...

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    // Helper methods for adding and removing ratings


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public void setTimeClose(String timeClose) {
        this.timeClose = timeClose;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public String getTimeClose() {
        return timeClose;
    }


    public double getAverageRating() {
        double sum = 0;
        int size = this.getRatings().size();

        for (int i = 0; i < size; i++) {
            sum += this.getRatings().get(i).getValue();
        }

        double average = sum / size;
        if(average == 0.0){
            return 0;
        }
        // Format the average to have two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double formattedAverage = Double.parseDouble(decimalFormat.format(average));

        return formattedAverage;
    }

    public void addImage(Image image){
        this.images.add(image);
    }

    public int ratingCountOf(int rating){
        int count=0;
        for(int i =0;i<this.ratings.size();i++){
            if(rating == (int) this.ratings.get(i).getValue()){
                count++;
            }
        }
        return count;
    }
}

