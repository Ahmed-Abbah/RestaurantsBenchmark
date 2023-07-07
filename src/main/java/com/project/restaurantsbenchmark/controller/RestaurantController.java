package com.project.restaurantsbenchmark.controller;

import com.project.restaurantsbenchmark.model.Image;
import com.project.restaurantsbenchmark.model.Restaurant;

import com.project.restaurantsbenchmark.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
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
    public String addRestaurant(Model model) {

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
    public String welcome(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "addNewRestaurant";
    }

    @PostMapping("/addRating")
    public String addRating(Model model, @RequestParam("restaurantId") long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "You Okay";
    }




    @PostMapping("/saveRestaurant")
    public String saveRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, @RequestParam("file") List<MultipartFile> files) throws IOException {
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
        restaurantService.saveRestaurant(restaurant);

        return "redirect:/";
    }




}
