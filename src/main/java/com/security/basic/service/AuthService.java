package com.security.basic.service;

import com.security.basic.dto.LoginDto;
import com.security.basic.dto.RegisterDto;
import com.security.basic.entity.Role;
import com.security.basic.entity.User;
import com.security.basic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;



    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
    }

    public String register(RegisterDto registerDto) {
        try{

            User user = User.builder()
                    .name(registerDto.getName())
                    .email(registerDto.getEmail())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(user);

            return "Success";
        }catch (Exception e){
            return e.getMessage();
        }
    }


    public String login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
            return jwtService
                    .createToken(userDetails, new HashMap<>());
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
