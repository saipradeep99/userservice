package com.example.userservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@JsonDeserialize(as=Session.class)
public class Session extends BaseModel {
    private String token;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date expiryAt;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
}
