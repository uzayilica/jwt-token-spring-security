package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


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


    @PostMapping("/loginn")
    public ResponseEntity<?> login (@RequestParam String username , @RequestParam String password){
        //login kontrolü yapılır ve SecurityContext Holder'a ekleme işlemi gerçekleştirilir.
       try {
           UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
           // Kullanıcı zaten var, işlemleri yapalım şifresi doğru mu bakalım
           if(passwordEncoder.matches(password,userDetails.getPassword())){

               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                       userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);

               return  ResponseEntity.status(HttpStatus.OK).body("KULLANICI GİRİŞİ BAŞAIRLI");

           }
           else {
               return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("şifre hatalı");
           }
       }
       catch (Exception e){
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("KULLANICI kayıtlı değil");
       }

    }




}
