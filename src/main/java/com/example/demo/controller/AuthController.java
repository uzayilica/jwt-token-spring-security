package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/auth/v1")
@RestController
public class AuthController {

    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(MyUserDetailsService myUserDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users users) {
        // Kullanıcı veri tabanında var mı yok mu kontrol edelim
        try {
            myUserDetailsService.loadUserByUsername(users.getUsername());
            // Kullanıcı zaten var, çakışma durumu
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Kullanıcı zaten mevcut.");
        } catch (UsernameNotFoundException e) {
            // Kullanıcı bulunamadı, kayıt işlemlerini yapalım
            Users newuser =new Users();
            newuser.setUsername(users.getUsername());
            newuser.setPassword(passwordEncoder.encode(users.getPassword()));
            newuser.setEmail(users.getEmail());
            newuser.setRole(users.getRole());
            userRepository.save(newuser);
            return ResponseEntity.status(HttpStatus.CREATED).body("Kullanıcı başarıyla kaydedildi.");
        } catch (Exception e) {
            // Diğer beklenmeyen hatalar için genel hata yönetimi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu: " + e.getMessage());
        }
    }




}
