package com.ppdm.backend.projects.dto;

import com.ppdm.backend.user.UserEntity;
import com.ppdm.backend.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private Long budget;
    private LocalDateTime createdAt;
    private Boolean active;
    private String officeLatitude;
    private String officeLongitude;
    private List<UserDto> members;
}
