package com.project.restaurantsbenchmark.controller;


import com.project.restaurantsbenchmark.service.UserServiceImpl;
import com.project.restaurantsbenchmark.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService ;

    @GetMapping("/User")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "Register&Login";
    }
    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        if (!user.getPassword().equals(user.getConfirmedPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "Register&Login";
        }

        userService.saveUser(user);
        return "Register&Login";
    }

    @PostMapping("/user/login")
    public String logUser(@RequestParam("login") String login,@RequestParam("pass") String pass, Model model) {
        if(userService.findUser(login,pass) == true){
            return "you logged successfully";
        }
        return "Oops!";

    }

}
