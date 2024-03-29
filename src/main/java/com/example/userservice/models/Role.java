package com.example.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
@JsonDeserialize(as = Role.class)
public class Role extends BaseModel {
    private String role;
}
