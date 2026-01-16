package com.ppdm.backend.security.controller;

import com.ppdm.backend.security.config.JwtTokenService;
import com.ppdm.backend.user.UserService;
import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.exception.LoginFaildException;
import com.ppdm.backend.user.exception.LogoutFaildException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthentificationController {

    private  final JwtTokenService jwtTokenService;
    private final UserService userService;

    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserDto user;
        try {
            user = userService.login(request);
        } catch (LoginFaildException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }

        String jwt = jwtTokenService.createJwtToken(user.getId(), user.getUsername());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", jwt);
        responseBody.put("id", user.getId());
        responseBody.put("username", user.getUsername());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable Long id) {
        try{
            userService.logout(id);
            return ResponseEntity.ok().build();
        }catch (LogoutFaildException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health(){
        return ResponseEntity.ok().build();
    }
}
