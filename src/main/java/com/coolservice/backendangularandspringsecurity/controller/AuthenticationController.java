package com.coolservice.backendangularandspringsecurity.controller;

import com.coolservice.backendangularandspringsecurity.dto.AuthenticationDto;
import com.coolservice.backendangularandspringsecurity.model.User;
import com.coolservice.backendangularandspringsecurity.security.jwt.utils.JwtProvider;
import com.coolservice.backendangularandspringsecurity.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:4200")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationDto authenticationDto) {
        Map<Object, Object> response = new HashMap<>();
        try {
            String username = authenticationDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authenticationDto.getPassword()));
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User - " + username + " not found");
            }

            if (!user.isNotLocked()) {
                return new ResponseEntity<>("User " + username + " is blocked", HttpStatus.GONE);
            }

            authenticationDto.setRoles(user.getRoles());
            String token = jwtProvider.createToken(username, user.getRoles());
            response.put("username", username);
            response.put("token", token);
            response.put("roles", authenticationDto.getRoles());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
