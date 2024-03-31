package com.example.userservice.dtos;

import lombok.Data;

@Data
public class SendEmailMessageDto {
    private String from;
    private String to;
    private String subject;
    private String body;
}
