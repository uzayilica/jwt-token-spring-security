package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRespository extends JpaRepository<Users,Integer> {
}
