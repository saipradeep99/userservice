package com.example.userservice.services;

import com.example.userservice.dtos.UserDto;
import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserDto getUserDetails(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user=userOptional.get();
        return UserDto.from(user);
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds){
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty())  return null;
        User user=userOptional.get();
        Set<Role> roleSet = roles.stream().collect(Collectors.toSet());
        user.setRoles(roleSet);
        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

}
