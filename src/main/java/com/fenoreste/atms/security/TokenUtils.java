package com.fenoreste.atms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Key;
import java.util.*;

@Slf4j
public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "586E3272357538YF41j3F4428472B4B6250655S368566DF597033733676397924";
    private final static Long ACCESS_TOKEN_EXPIRES_IN = 1500L;

    public static String generateAccessToken(String username,String password) {
        long expiresIn = ACCESS_TOKEN_EXPIRES_IN * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expiresIn);

        log.info("Expiracion token: " + expirationDate);
        Map<String,Object> extra = new HashMap<>();
        extra.put("username",username);
        extra.put("password",password);

        return Jwts.builder()
                .setSubject(password)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();

    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email,null,Collections.emptyList());
        }catch(JwtException e) {
            return null;
        }
    }


    public static long getExpireInToken(String token) {
       Key key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());


       Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Obtener la fecha de expiración del token
        Date expirationDate = claims.getExpiration();
        System.out.println("Expiration Date: " + expirationDate);

        return expirationDate.getTime();
    }

    
   

}
