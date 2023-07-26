package com.project.restaurantsbenchmark.controller;

import com.project.restaurantsbenchmark.model.Image;
import com.project.restaurantsbenchmark.model.Rating;
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
import java.util.Date;
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
    @GetMapping("/manageRestaurants")
    public String manageRestaurants(Model model){
        List<Restaurant> restaurants = restaurantService.SelectAllRestaurantsNotApproved();
        model.addAttribute("restaurants",restaurants);
        return "RestaurantsList";
    }

    @PostMapping ("/deleteRestaurant")
    public String deleteRestaurant(@RequestParam("restaurantIdtoDelete") long restaurantId){
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        restaurant.setStatus(null);
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/manageRestaurants";
    }

    @GetMapping("/approveRestaurant")
    public String approveRestaurant(@RequestParam("restaurantIdtoApprove") long restaurantId){
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        restaurant.setStatus("approved");
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/manageRestaurants";
    }
    @GetMapping("/compare")
    public String compareRestaurants(Model model,HttpSession session) {
        session.setAttribute("compareMessage","please chooose the first restaurant to compare !");
        session.removeAttribute("FirstRestaurant");
        session.removeAttribute("SecondeRestaurant");
        List<Restaurant> restaurants =restaurantService.SelectAllRestaurants() ;
        for(int i = 0;i<restaurants.size();i++){
            if(restaurants.get(i).getRatings().size()==0){
                restaurants.remove(i);
            }
        }
//        for(Restaurant restaurant : restaurants){
//            if(!restaurant.getImages().isEmpty()){
//                for(Image image: restaurant.getImages()){
//                    image.setImageUrl(folderPath+image.getImageUrl());
//                }
//            }
//        }
        model.addAttribute("restaurants",restaurants);

        return "compare";
    }

    @GetMapping("/compareRestaurant")
    public String compare(Model model,@RequestParam("CompareRestaurant") long restaurantNumber1,HttpSession session) {

        if(session.getAttribute("FirstRestaurantTemp") == null){

            session.setAttribute("compareMessage","please chooose the seconde restaurant to compare !");
            session.setAttribute("FirstRestaurantTemp",restaurantService.findRestaurant(restaurantNumber1));
            List<Restaurant> restaurants =restaurantService.SelectAllRestaurants() ;

            for(int i=0;i<restaurants.size();i++){

                if(restaurants.get(i).getId().equals(restaurantService.findRestaurant(restaurantNumber1).getId()) ){
                    restaurants.remove(i);
                }
                if( restaurants.get(i).getRatings().size()==0){
                    restaurants.remove(i);

                }
            }
            model.addAttribute("restaurants",restaurants);

            return "compare";
        }else if(session.getAttribute("SecondeRestaurant") == null){
            session.setAttribute("SecondeRestaurant",restaurantService.findRestaurant(restaurantNumber1));
            session.setAttribute("FirstRestaurant",session.getAttribute("FirstRestaurantTemp"));
            session.removeAttribute("FirstRestaurantTemp");


            return "showComparaison";
        }

        List<Restaurant> restaurants =restaurantService.SelectAllRestaurants() ;
        model.addAttribute("restaurants",restaurants);
        return "compare";
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
        List<Restaurant> restaurants = restaurantService.SelectAllRestaurants();
        model.addAttribute("rating",new Rating());
        model.addAttribute("restaurants",restaurants);
        return "AddRating";

        /*if(session.getAttribute("userLoggedIn")!=null){
            return "AddRating";
        }else{
            return"redirect:/user";
        }*/
    }

    @PostMapping("/saveRating")
    public String saveRating(Model model,@ModelAttribute("rating") Rating rating, @RequestParam("restaurantId") long restaurantId,HttpSession session) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        rating.setDateMade(new Date());
        User user = (User)session.getAttribute("userLoggedIn");
        if (user == null) {
            return "redirect:/user?logInPageMesssage=please+log+in+order+to+add+a+rating";
        }

        if (rating.getComment().isEmpty() && rating.getValue() == 0.0) {
            return "redirect:/addRating?restaurantId=" + restaurantId + "&ratingPageMessage=please+add+a+rating+or+comment";
        }


        restaurant.getRatings().add(rating);
        rating.setRestaurant(restaurant);
        rating.setUserId(user.getId());
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/addRating?restaurantId=" + restaurantId + "&RatingAddedSuccessfully=Thanks,+your+rating+has+been+added+successfully.";

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
