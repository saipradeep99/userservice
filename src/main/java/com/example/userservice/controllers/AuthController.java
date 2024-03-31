package com.example.userservice.controllers;

import com.example.userservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
}
