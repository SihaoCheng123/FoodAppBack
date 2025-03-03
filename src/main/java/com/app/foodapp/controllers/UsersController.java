package com.app.foodapp.controllers;

import com.app.foodapp.dto.ApiDelivery;
import com.app.foodapp.dto.LoginRequest;
import com.app.foodapp.dto.LoginResponse;
import com.app.foodapp.models.Users;
import com.app.foodapp.services.UsersService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    /*@PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody User userFromReactNative){
        Users user = new Users();
        user.setFirstName("Lara");
        user.setLastName("Larita");
        user.setEmail("lalala@gmail.com");
        user.setPassword("lalita");
        user.setImage("heh.png");
        user.setPhone("61231273");

        return ResponseEntity.ok(user);
    }*/

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> users = this.usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody Users user){
        Users createUser = this.usersService.createUser(user);
        return ResponseEntity.ok(createUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest credentials){
        ApiDelivery<LoginResponse> response = this.usersService.login(credentials.getEmail(), credentials.getPassword());
       return ResponseEntity.status(response.getStatus()).body(response);
    }

}
