package com.project.restaurantsbenchmark.service;
import com.project.restaurantsbenchmark.repository.UserRepository;
import com.project.restaurantsbenchmark.model.User;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository ;
    public void saveUser(User user){
         this.userRepository.save(user);
    }

    public boolean findUser(String login, String pass) {
        List<User> users = this.userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(login) && user.getPassword().equals(pass)) {
                return true;
            }
        }
        return false;
    }


}
