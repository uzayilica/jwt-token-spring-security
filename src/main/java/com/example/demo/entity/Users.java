package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Role;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(name = "email",columnNames = "email"))
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(unique = true)

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
