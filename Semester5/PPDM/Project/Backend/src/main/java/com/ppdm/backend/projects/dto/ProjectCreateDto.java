package com.ppdm.backend.projects.dto;

import lombok.Data;

@Data
public class ProjectCreateDto {
    private String name;
    private Long budget;
    private Boolean active;
    private String officeLatitude;
    private String officeLongitude;
    private Long creatorId;
}
