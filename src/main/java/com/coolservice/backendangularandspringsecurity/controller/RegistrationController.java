package com.coolservice.backendangularandspringsecurity.controller;

import com.coolservice.backendangularandspringsecurity.dto.UserDto;
import com.coolservice.backendangularandspringsecurity.model.User;
import com.coolservice.backendangularandspringsecurity.security.jwt.utils.JwtProvider;
import com.coolservice.backendangularandspringsecurity.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class RegistrationController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public RegistrationController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<Object> register(@RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        User user;

        if (userDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (!userDto.getConfirmPassword().equals(userDto.getPassword())) {
            return new ResponseEntity<>("Password and Confirmed password not equals", HttpStatus.CONFLICT);
        }

        try {
            userService.register(userDto.toUser());
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("User " + userDto.getUsername() + " is exists", HttpStatus.CONFLICT);
        }

        user = userService.findByUsername(userDto.getUsername());
        if (user == null) {
            return new ResponseEntity<>("Register error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String token = jwtProvider.createToken(user.getUsername(), user.getRoles());
        response.put("username", userDto.getUsername());
        response.put("token", token);
        response.put("roles", user.getRoles());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
