package com.ppdm.backend.user.dto;

import lombok.Data;

import javax.swing.text.StyledEditorKit;

@Data
public class UserUpdateDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
    private String latitude;
    private String longitude;
}
