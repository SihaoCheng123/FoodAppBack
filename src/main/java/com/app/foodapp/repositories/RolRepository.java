package com.app.foodapp.repositories;

import com.app.foodapp.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Roles, Long> {

    Roles findRolByName(String name);
}
