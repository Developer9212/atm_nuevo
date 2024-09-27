package com.fenoreste.atms.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenoreste.atms.modelo.AuthResponse;
import com.fenoreste.atms.modelo.AuthRequestUser;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        AuthRequestUser authCredentials = new AuthRequestUser();
        try {
            authCredentials = new ObjectMapper().readValue(req.getReader(), AuthRequestUser.class);
        }catch (IOException e){

        }



        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
               authCredentials.getUser(),
                authCredentials.getPwd(),
                Collections.emptyList()
        );
       return getAuthenticationManager().authenticate(usernamePAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl)authResult.getPrincipal();
        String token = TokenUtils.generateAccessToken(userDetails.getNombre(),userDetails.getUsername());
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        AuthResponse responseAuth = new AuthResponse();
        long expireIn = TokenUtils.getExpireInToken(token);
        if(token != null){
            responseAuth.setToken(token);
            responseAuth.setSuccess(true);
            responseAuth.setExpiresIn(expireIn);
        }
        objectMapper.writeValue(res.getWriter(), responseAuth);
        super.successfulAuthentication(req, res, chain, authResult);
    }
}
