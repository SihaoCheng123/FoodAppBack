package com.app.foodapp.controllers;

import com.app.foodapp.models.Users;
import com.app.foodapp.services.UsersService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/create")
    public ResponseEntity<Users> createUser(){
        Users user = new Users();
        user.setFirstName("Lara");
        user.setLastName("Larita");
        user.setEmail("lalala@gmail.com");
        user.setPassword("lalita");
        user.setImage("heh.png");
        user.setPhone("61231273");

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> users = this.usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
