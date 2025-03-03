package com.app.foodapp.services;

import com.app.foodapp.models.Roles;
import com.app.foodapp.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    @Autowired
    private RolRepository rolRepository;

    public Roles getRolByName(String name){
        return this.rolRepository.findRolByName(name);
    }

    public List<Roles> getAllRoles(){
        return this.rolRepository.findAll();
    }

    public Roles createRol(Roles rol){
        if (this.rolRepository.findRolByName(rol.getName()) != null){
            throw new RuntimeException("Rol ya existe");
        }
        Roles newRol = new Roles();
        newRol.setName(rol.getName());
        newRol.setDescription(rol.getDescription());
        newRol.setImage("");
        newRol.setRoute(rol.getRoute());

        return this.rolRepository.save(newRol);
    }
}
