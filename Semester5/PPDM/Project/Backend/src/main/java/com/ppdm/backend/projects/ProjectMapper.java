package com.ppdm.backend.projects;

import com.ppdm.backend.projects.dto.ProjectCreateDto;
import com.ppdm.backend.projects.dto.ProjectDto;
import com.ppdm.backend.projects.dto.ProjectUpdateDto;
import com.ppdm.backend.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProjectMapper {
    ProjectDto entityToDto(ProjectEntity userEntity);

    List<ProjectDto> entityToDto(List<ProjectEntity> userEntities);

    ProjectEntity dtoToEntity(ProjectDto dto);

    ProjectUpdateDto projectUpdateDtoFromEntity(ProjectEntity user);

    ProjectEntity projectCreateDtoToEntity(ProjectCreateDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    void entityFromProjectUpdateDto(ProjectUpdateDto dto, @MappingTarget ProjectEntity entity);
}