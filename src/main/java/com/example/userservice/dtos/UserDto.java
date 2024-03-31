package com.example.userservice.dtos;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import jakarta.persistence.ManyToMany;

import java.util.Set;

public class UserDto {
    private String email;
    @ManyToMany
    private Set<Role> roles;

    public static UserDto from(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();
        userDto.email = user.getEmail();
        userDto.roles = user.getRoles();
        return userDto;
    }
}
