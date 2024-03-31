package com.example.userservice.services;

import com.example.userservice.clients.KafkaProducerClient;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.SessionRepository;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProducerClient kafkaProducerClient;
}
