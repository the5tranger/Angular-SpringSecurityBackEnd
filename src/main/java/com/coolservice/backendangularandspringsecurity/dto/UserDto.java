package com.coolservice.backendangularandspringsecurity.dto;

import com.coolservice.backendangularandspringsecurity.model.User;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public boolean isEmpty() {
        return StringUtils.isAnyEmpty(username, firstName, lastName, email, password);
    }
}
