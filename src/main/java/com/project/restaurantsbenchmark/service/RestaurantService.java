package com.project.restaurantsbenchmark.service;

import com.project.restaurantsbenchmark.model.Restaurant;
import com.project.restaurantsbenchmark.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> SelectAllRestaurants() {
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        List<Restaurant> approvedRestaurants = new ArrayList<>();

        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.getStatus()!=null) {
                approvedRestaurants.add(restaurant);
            }
        }

        return approvedRestaurants;
    }




    public List<Restaurant> SelectAllRestaurantsNotApproved(){
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants;
    }

    public Restaurant findRestaurant(long restaurantId){
        Optional<Restaurant> restaurant ;
        restaurant = restaurantRepository.findById(restaurantId);
        return restaurant.orElse(null);
    }

    public void deleteRestaurant(long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

}

