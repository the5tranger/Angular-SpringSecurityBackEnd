package com.coolservice.backendangularandspringsecurity.service.impl;

import com.coolservice.backendangularandspringsecurity.model.Role;
import com.coolservice.backendangularandspringsecurity.model.User;
import com.coolservice.backendangularandspringsecurity.repository.RoleRepository;
import com.coolservice.backendangularandspringsecurity.repository.UserRepository;
import com.coolservice.backendangularandspringsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User register(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setNotLocked(true);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
