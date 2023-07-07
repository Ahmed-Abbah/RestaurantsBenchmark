    package com.project.restaurantsbenchmark.model;

    import jakarta.persistence.*;



    @Entity
    @Table(name = "images")
    public class Image {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Restaurant getRestaurant() {
            return restaurant;
        }

        @Column(nullable = false)
        private String imageUrl;

        @ManyToOne
        @JoinColumn(name = "restaurant_id", nullable = false)
        private Restaurant restaurant;



        public Image() {
        }

        public Image(String imageUrl, Restaurant restaurant) {
            this.imageUrl = imageUrl;
            this.restaurant = restaurant;
        }


    }

