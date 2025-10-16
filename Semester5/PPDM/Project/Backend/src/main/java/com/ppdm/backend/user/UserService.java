package com.ppdm.backend.user;

import com.ppdm.backend.security.controller.LoginRequest;
import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.dto.UserUpdateDto;
import com.ppdm.backend.user.exception.InvalidUserException;
import com.ppdm.backend.user.exception.LoginFaildException;
import com.ppdm.backend.user.exception.UserAlreadyExistException;
import com.ppdm.backend.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public UserDto findUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return mapper.entityToDto(user);
    }

    public List<UserDto> findAllUsers() {
        return mapper.entityToDto(userRepository.findAllByOrderById());
    }

    public UserDto saveUser(UserDto userDto) {
        UserEntity user = mapper.dtoToEntity(userDto);

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException();
        }
        return mapper.entityToDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UserUpdateDto userDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        boolean usernameChanged = applyUpdates(user, userDto);

        if(usernameChanged && userRepository.existsByUsername(user.getUsername())) {
            throw new InvalidUserException("Username already exists!");
        }

        UserEntity updatedEntity = userRepository.save(user);
        simpMessagingTemplate.convertAndSend("/ws/user-updated", mapper.userUpdateDtoFromEntity(updatedEntity));
        return mapper.entityToDto(updatedEntity);
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
        UserEntity user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            throw new LoginFaildException("There is no user with the username: " + request.getUsername());
        }
        if(!request.getPassword().equals(user.getPassword())) {
            throw new LoginFaildException("Wrong password");
        }
        return mapper.entityToDto(user);
    }

    public void isActive(Long id) {
        userRepository.activateUser(id);
    }
}
