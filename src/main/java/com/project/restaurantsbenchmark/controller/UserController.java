package com.project.restaurantsbenchmark.controller;


import com.project.restaurantsbenchmark.service.UserServiceImpl;
import com.project.restaurantsbenchmark.model.User;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService ;

    @GetMapping("/user")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "Register&Login";
    }

    @GetMapping("/manageUsers")
    public String showAdminPanelUsers(Model model){
        List<User> users = userService.SelectAllUsers();
        model.addAttribute("users",users);
        return "UsersList";
    }



    @PostMapping("/user/register")
    public String saveUser(@ModelAttribute("user") User user, Model model,HttpSession session ) {
        if (!user.getPassword().equals(user.getConfirmedPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "redirect:/user";
        }

        session.setAttribute("userLoggedIn",user);
        userService.saveUser(user);
        return "redirect:/";
    }

    @PostMapping("/user/login")
    public String logUser(@RequestParam("login") String login,@RequestParam("pass") String pass, Model model,HttpSession session) {
        User user = userService.findUser(login,pass);
        if(user != null){
            session.setAttribute("userLoggedIn",user);
            if(user.getIsAdmin().equals("yes")){
                return "redirect:/manageRestaurants";
            }else{
                return "redirect:/";
            }

        }
        return "redirect:/user";
    }

    @GetMapping("/user/logout")
    public String logoutUser(HttpSession session) {
        session.setAttribute("userLoggedIn",null);
        return "redirect:/";
    }

}
