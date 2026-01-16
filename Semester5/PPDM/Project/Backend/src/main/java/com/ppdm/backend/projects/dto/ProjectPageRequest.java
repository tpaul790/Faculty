package com.ppdm.backend.projects.dto;

import lombok.Data;

@Data
public class ProjectPageRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private String name;
    private Boolean onlyActive;
}
