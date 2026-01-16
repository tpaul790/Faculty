package com.ppdm.backend.user;

import com.ppdm.backend.projects.ProjectEntity;
import com.ppdm.backend.projects.ProjectMapper;
import com.ppdm.backend.projects.ProjectRepository;
import com.ppdm.backend.projects.ProjectSpecifications;
import com.ppdm.backend.projects.dto.ProjectDto;
import com.ppdm.backend.projects.dto.ProjectPageRequest;
import com.ppdm.backend.security.controller.LoginRequest;
import com.ppdm.backend.user.dto.UserCreateDto;
import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.dto.UserPageRequest;
import com.ppdm.backend.user.dto.UserUpdateDto;
import com.ppdm.backend.user.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    public UserDto findUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.entityToDto(user);
    }

    public UserDto saveUser(UserCreateDto userDto) {
        UserEntity user = userMapper.userCreateDtoToEntity(userDto);

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException();
        }

        return userMapper.entityToDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UserUpdateDto userDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        boolean usernameChanged = applyUpdates(user, userDto);

        if(usernameChanged && userRepository.existsByUsername(user.getUsername())) {
            throw new InvalidUserException("Username already exists!");
        }

        UserEntity updatedEntity = userRepository.save(user);
        return userMapper.entityToDto(updatedEntity);
    }

    private boolean applyUpdates(UserEntity user, UserUpdateDto userDto) {
        boolean result = !user.getUsername().equals(userDto.getUsername());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return result;
    }


    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public UserDto login(LoginRequest request) {
        Optional<UserEntity> op = userRepository.findByUsername(request.getUsername());
        if(op.isEmpty()) {
            throw new LoginFaildException("There is no user with the username: " + request.getUsername());
        }
        if(!request.getPassword().equals(op.get().getPassword())) {
            throw new LoginFaildException("Wrong password");
        }
        UserEntity user = op.get();
        user.setActive(true);
        return userMapper.entityToDto(userRepository.save(user));
    }

    public void logout(Long id) {
        Optional<UserEntity> op = userRepository.findById(id);
        if(op.isEmpty()) {
            throw new LogoutFaildException("There is no user with the id: " + id);
        }
        UserEntity user = op.get();
        user.setActive(false);
        userRepository.save(user);
    }

    public List<UserDto> findAll() {
        return userMapper.entityToDto(userRepository.findAll());
    }

    public Page<ProjectEntity> findUserProjects(Long id, ProjectPageRequest request) {
        Integer pageNumber = request.getPageNumber();
        Integer pageSize = request.getPageSize();
        String name = request.getName();
        Boolean onlyActive = request.getOnlyActive();

        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<ProjectEntity> spec = ProjectSpecifications.isMember(id);

        if (Boolean.TRUE.equals(onlyActive)) {
            spec = spec.and(ProjectSpecifications.isActive());
        }

        if (name != null && !name.isEmpty()) {
            spec = spec.and(ProjectSpecifications.inName(name));
        }

        return projectRepository.findAll(spec, pageable);
    }
}
