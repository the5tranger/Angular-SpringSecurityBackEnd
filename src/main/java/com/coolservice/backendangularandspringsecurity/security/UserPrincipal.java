package com.coolservice.backendangularandspringsecurity.security;

import com.coolservice.backendangularandspringsecurity.model.User;
import com.coolservice.backendangularandspringsecurity.security.jwt.JwtUser;
import com.coolservice.backendangularandspringsecurity.security.jwt.JwtUserFactory;
import com.coolservice.backendangularandspringsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserPrincipal implements UserDetailsService {
    private final UserService userService;

    public UserPrincipal(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User - " + username + " not found");
        }

        return JwtUserFactory.create(user);
    }
}
