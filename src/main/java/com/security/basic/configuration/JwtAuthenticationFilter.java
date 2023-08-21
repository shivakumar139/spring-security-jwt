package com.security.basic.configuration;

import com.security.basic.service.AuthService;
import com.security.basic.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // extract token from Authorization Header
        String token = request.getHeader("Authorization");

        // if token is not present
        if(token == null || !token.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        // verify token is valid or not
        token = token.substring(7);
        if(!jwtService.isValidToken(token)){
            // if not valid token
            filterChain.doFilter(request, response);
            return;
        }


        // user must not be already authenticated
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            // extract email from token and find user from database
            String userEmail = jwtService.extractUserName(token);

            UserDetails user = authService.findByEmail(userEmail);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                    user,null,user.getAuthorities()
            );

//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }

        filterChain.doFilter(request, response);


    }




}
