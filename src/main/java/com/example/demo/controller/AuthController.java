package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/auth/v1")
@RestController
public class AuthController {

    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(MyUserDetailsService myUserDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Kullanıcıyı veritabanından çek
            Users user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            // Şifre kontrolü
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Kimlik doğrulama başarılı, JWT token oluştur
                String token = jwtService.generateToken(user);

                // Authentication nesnesini oluştur ve SecurityContext'e set et
                // Users sınıfınızda getRole() metodu olduğunu varsayıyorum
                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Token'i response body'de döndür
                Map<String, String> response = new HashMap<>();
                response.put("message", "Kullanıcı girişi başarılı");
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Şifre hatalı");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı kayıtlı değil");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu: " + e.getMessage());
        }
    }   }