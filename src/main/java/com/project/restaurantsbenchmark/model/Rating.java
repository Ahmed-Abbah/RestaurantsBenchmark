package com.project.restaurantsbenchmark.model;


import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "ratings")
public class Rating {

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

    // Getters and setters

    // ...

}

