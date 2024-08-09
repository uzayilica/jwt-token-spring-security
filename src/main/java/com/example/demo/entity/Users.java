package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Role;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;


    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
