package com.ppdm.backend.projects;

import com.ppdm.backend.projects.dto.ProjectCreateDto;
import com.ppdm.backend.projects.dto.ProjectDto;
import com.ppdm.backend.projects.dto.ProjectUpdateDto;
import com.ppdm.backend.projects.exception.ProjectNotFoundException;
import com.ppdm.backend.user.UserEntity;
import com.ppdm.backend.user.UserRepository;
import com.ppdm.backend.user.exception.UserNotFoundException;
import com.ppdm.backend.websockets.ProjectWebSocketHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper mapper;
    private final ProjectWebSocketHandler webSocketHandler;

    @Transactional
    public ProjectDto saveProject(ProjectCreateDto dto) {
        ProjectEntity project = mapper.projectCreateDtoToEntity(dto);
        List<UserEntity> members = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        members.add(currentUser);
        project.setMembers(members);
        project.setCreatedAt(LocalDateTime.now());

        ProjectEntity result = projectRepository.save(project);
        webSocketHandler.broadcast("USER_ADDED");
        return mapper.entityToDto(result);
    }

    @Transactional
    public ProjectDto updateProject(Long id, ProjectUpdateDto projectDto) {
        ProjectEntity project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        mapper.entityFromProjectUpdateDto(projectDto, project);

        List<Long> newMemberIds = projectDto.getNewMemberIds() == null ? new ArrayList<>() : projectDto.getNewMemberIds();
        List<UserEntity> managedNewMembers = userRepository.findAllById(newMemberIds);
        project.getMembers().addAll(managedNewMembers);

        ProjectEntity updatedProject = projectRepository.save(project);
        webSocketHandler.broadcast("USER_UPDATED");

        return mapper.entityToDto(updatedProject);
    }
}
