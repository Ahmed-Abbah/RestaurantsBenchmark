package com.project.restaurantsbenchmark.model;


import jakarta.persistence.*;


import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "ratings")
public class Rating {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDateMade(Date dateMade) {
        this.dateMade = dateMade;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public double getValue() {
        return value;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Long getUserId() {
        return userId;
    }

    public Date getDateMade() {
        return dateMade;
    }

    public String getComment() {
        return Comment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double value;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Date dateMade;

    @Column(nullable = false)
    private String Comment;

    // Constructors, getters, and setters

    public Rating() {
    }

    public Rating(double value, Restaurant restaurant, Long userId, Date dateMade) {
        this.value = value;
        this.restaurant = restaurant;
        this.userId = userId;
        this.dateMade = dateMade;
    }

    public String getFormattedDate() {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        return outputFormat.format(dateMade);
    }

    // Getters and setters

    // ...

}

