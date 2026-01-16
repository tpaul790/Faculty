package com.ppdm.backend.user.dto;

import lombok.Data;

@Data
public class UserPageRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private String username;
    private Boolean onlyActive;
}
