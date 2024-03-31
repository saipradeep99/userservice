package com.example.userservice.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SetUserRolesRequestDto {
    private List<Long> roleIds;
}
