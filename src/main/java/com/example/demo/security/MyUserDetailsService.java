package com.example.demo.security;

import com.example.demo.entity.Users;
import com.example.demo.repository.UserRespository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRespository userRespository;

    public MyUserDetailsService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRespository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
       return new MyUserDetails(users);
    }
}
