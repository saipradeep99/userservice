package com.example.userservice.dtos;

import lombok.Data;

@Data
public class ValidateTokenRequestDto {
    private String token;
    private Long userId;
}
