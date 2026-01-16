package com.ppdm.backend.projects.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectUpdateDto {
    private String name;
    private Long budget;
    private Boolean active;
    private String officeLatitude;
    private String officeLongitude;
    private List<Long> newMemberIds;

    @Override
    public String toString(){
        return name + " " + budget + " " + active;
    }
}
