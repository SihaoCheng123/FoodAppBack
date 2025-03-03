package com.app.foodapp.controllers;

import com.app.foodapp.models.Roles;
import com.app.foodapp.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;

//    @GetMapping("/create")
//    public ResponseEntity<Roles> createRol(){
//        Roles rol = new Roles();
//        rol.setName("Profesor");
//        rol.setImage("");
//        rol.setRoute("");
//        rol.setDescription("Rol de profesor");
//        return ResponseEntity.ok(rol);
//    }

    @GetMapping
    public ResponseEntity<List<Roles>> getAllRoles(){
        List<Roles> roles = this.rolesService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/create")
    public ResponseEntity<Roles> createRol(@RequestBody Roles roles){
        Roles createRol = this.rolesService.createRol(roles);
        return ResponseEntity.ok(createRol);
    }
}
