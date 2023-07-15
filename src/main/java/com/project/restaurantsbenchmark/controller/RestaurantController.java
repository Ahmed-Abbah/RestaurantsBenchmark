package com.project.restaurantsbenchmark.controller;

import com.project.restaurantsbenchmark.model.Image;
import com.project.restaurantsbenchmark.model.Restaurant;

import com.project.restaurantsbenchmark.model.User;
import com.project.restaurantsbenchmark.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class RestaurantController {
    @Autowired
    private final RestaurantService restaurantService;
    @Value("${uploadedImagesDirectory}")
    private String ImagesUploadPrimaryFolder ;
    // i store images in two places(in target and src beceause
    // storing images in only src folder will need to re run
    // the project in order to see the newly added images
    @Value("${secondeImagesDirectory}")
    private String ImagesUploadSecondaryFolder ;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/")
    public String listRestaurants(Model model) {

        List<Restaurant> restaurants =restaurantService.SelectAllRestaurants() ;
//        for(Restaurant restaurant : restaurants){
//            if(!restaurant.getImages().isEmpty()){
//                for(Image image: restaurant.getImages()){
//                    image.setImageUrl(folderPath+image.getImageUrl());
//                }
//            }
//        }
        model.addAttribute("restaurants",restaurants);

        return "welcome";
    }
    @GetMapping("/addRestaurant")
    public String welcome(Model model, HttpSession session) {
        if(session.getAttribute("userLoggedIn") !=null){
            model.addAttribute("restaurant", new Restaurant());
            return "addNewRestaurant";

        }else{
            return "redirect:/user";
        }

    }

    @GetMapping("/addRating")
    public String addRating(Model model, @RequestParam("restaurantId") long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "AddRating";

        /*if(session.getAttribute("userLoggedIn")!=null){
            return "AddRating";
        }else{
            return"redirect:/user";
        }*/
    }
    @PostMapping("/saveRestaurant")
    public String saveRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam("file") List<MultipartFile> files,HttpSession session) throws IOException {
        List<Image> images = new ArrayList<>();
        Random random = new Random();

        for (MultipartFile file : files) {
            Image image = new Image();
            image.setRestaurant(restaurant);
            int randomToken = random.nextInt(9000) + 10000;
            image.setImageUrl("ImageN" + randomToken);

            String nameAndExtension[] = file.getOriginalFilename().split("\\.");
            String fileExtension = nameAndExtension[1];
            image.setImageUrl("ImageN" + randomToken + "." + fileExtension);

            file.transferTo(new File(ImagesUploadPrimaryFolder + image.getImageUrl() ));
            //here i save two times the images
            Path sourcePath = Path.of(ImagesUploadPrimaryFolder + image.getImageUrl());
            Path targetPath = Path.of(ImagesUploadSecondaryFolder + image.getImageUrl());
            Files.copy(sourcePath,targetPath,StandardCopyOption.REPLACE_EXISTING);
            images.add(image);
        }
        restaurant.setImages(images);
        User user =(User) session.getAttribute("userLoggedIn");
        restaurant.setUser(user);
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/";
    }




}
