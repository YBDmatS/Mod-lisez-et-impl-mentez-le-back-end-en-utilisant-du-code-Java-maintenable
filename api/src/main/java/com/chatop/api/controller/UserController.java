package com.chatop.api.controller;

import com.chatop.api.dto.UserMeResponseDto;
import com.chatop.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserMeResponseDto> getUserDetails(@PathVariable("userId") Long userId) {
        UserMeResponseDto response = userService.getCurrentUser(userId);
        return ResponseEntity.ok(response);
    }
}
