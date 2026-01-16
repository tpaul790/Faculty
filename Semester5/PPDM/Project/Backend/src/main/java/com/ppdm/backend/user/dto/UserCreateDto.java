package com.ppdm.backend.user.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean active;
}
