package com.fenoreste.atms.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {




        String barerToken = request.getHeader("Authorization");
        if (barerToken != null && barerToken.startsWith("Bearer ")) {
            barerToken = barerToken.replace("Bearer","");
            UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(barerToken);
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);

        }

        filterChain.doFilter(request, response);
    }
}
