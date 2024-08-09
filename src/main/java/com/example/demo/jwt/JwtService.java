package com.example.demo.jwt;

import com.example.demo.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    public String SecretCode="MDQyazNvY3c0dXVrcngwOXc4aDNib2Zja3Jlb2lybWczdnZzMW1mZWppM3g1bmZtanJkMTc0YnJ6M3l2c3U1NHV6OGw2a3h1ZjllczllY3Q2Mmk0cnFtcWZjZzRqa2RvaDMwemRuMTB1OHlzeTZ1dGJvNWMxdDAzY3phZXlibGY=";

    public String generateToken(Users user){
        return Jwts.builder()
                .signWith(getSignKey(SecretCode))
                .subject(user.getUsername())
                .claim("role",user.getRole().name())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .issuedAt(new Date())
                .compact();

    }
    public SecretKey getSignKey(String SecretCode){
        byte[] decode = Decoders.BASE64.decode(SecretCode);
        SecretKey secretKey = Keys.hmacShaKeyFor(decode);
        return secretKey;

    }
    public String  getUsername(String token){
      return  Jwts.parser()
                .verifyWith(getSignKey(SecretCode))
                .build()
                .parseSignedClaims(token).getPayload().getSubject();

    }

    public Date getExpirationDate(String token){
        return Jwts.parser().verifyWith(getSignKey(SecretCode))
                .build().parseSignedClaims(token).getPayload().getExpiration();
    }

     public boolean validateToken(String token, UserDetails user){
         final String username = getUsername(token);
         return (username.equals(user.getUsername()) && !isExpired(token));
     }

    public boolean isExpired(String token){
        return getExpirationDate(token).before(new Date());
    }
}
