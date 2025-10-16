package com.ppdm.backend.user.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
