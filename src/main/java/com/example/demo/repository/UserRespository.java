package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRespository extends JpaRepository<Users,Integer> {

    Optional<Users> findByUsername(String username);
}
