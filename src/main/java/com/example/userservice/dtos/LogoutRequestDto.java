package com.example.userservice.dtos;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String token;
    private Long userId;
}
