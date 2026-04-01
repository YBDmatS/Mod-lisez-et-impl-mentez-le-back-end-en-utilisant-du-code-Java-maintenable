package com.chatop.api.controller;

import com.chatop.api.dto.UserDetailsResponseDto;
import com.chatop.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user-related endpoints, such as retrieving user details.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to retrieve the details of a user by their ID.
     *
     * @param userId The ID of the user whose details are to be retrieved, extracted from the URL path variable.
     * @return HTTP 200 OK with a response containing the user's details if the user is found.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(@PathVariable("userId") Long userId) {
        UserDetailsResponseDto response = userService.getCurrentUser(userId);
        return ResponseEntity.ok(response);
    }
}
