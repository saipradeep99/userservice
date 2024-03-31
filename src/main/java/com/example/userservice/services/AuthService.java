package com.example.userservice.services;

import com.example.userservice.clients.KafkaProducerClient;
import com.example.userservice.dtos.SendEmailMessageDto;
import com.example.userservice.dtos.UserDto;
import com.example.userservice.models.User;
import com.example.userservice.models.Session;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.repositories.SessionRepository;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.http.ResponseEntity;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import org.springframework.util.MultiValueMapAdapter;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private KafkaProducerClient kafkaProducerClient;
    @Autowired
    private ObjectMapper objectMapper;

    public ResponseEntity<UserDto> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();
//        if (!new BCryptPasswordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Wrong username password");
//        }
        if(password!=user.getPassword()){
            throw new RuntimeException("Wrong username password");
        }
        MacAlgorithm alg = Jwts.SIG.HS256;
        SecretKey key = alg.key().build();
        Map<String, Object>  jsonForJwt = new HashMap<>();
        jsonForJwt.put("email", user.getEmail());
        jsonForJwt.put("createdAt", new Date());
        jsonForJwt.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));
        String token = Jwts.builder()
                .claims(jsonForJwt)
                .signWith(key, alg)
                .compact();
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);
        UserDto userDto = UserDto.from(user);
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty())  return null;
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isEmpty()) throw new RuntimeException("email already exists");;
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
//        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        UserDto userDto = UserDto.from(savedUser);
//        try {
//            kafkaProducerClient.sendMessage("userSignUp", objectMapper.writeValueAsString(userDto));
//            SendEmailMessageDto emailMessage = new SendEmailMessageDto();
//            emailMessage.setTo(userDto.getEmail());
//            emailMessage.setFrom("saipradeep@gmail.com");
//            emailMessage.setSubject("Welcome to Backend Project");
//            emailMessage.setBody("Thanks for creating an account.");
//            kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(emailMessage));
//        } catch (Exception e) {
//            System.out.println("Something has gone wrong with kafka");
//        }
        return userDto;
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty())  return SessionStatus.ENDED;
        Session session = sessionOptional.get();
        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE))   return SessionStatus.ENDED;
        Jws<Claims> claimsJws = Jwts.parser()
                .build()
                .parseSignedClaims(token);
        Date createdAt = (Date) claimsJws.getPayload().get("createdAt");
        if (createdAt.before(new Date()))   return SessionStatus.ENDED;
        return SessionStatus.ACTIVE;
    }
}
