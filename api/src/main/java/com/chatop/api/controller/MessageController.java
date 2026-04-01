package com.chatop.api.controller;

import com.chatop.api.dto.MessageCreateRequestDto;
import com.chatop.api.dto.StandardResponseDto;
import com.chatop.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling message-related endpoints.
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    public final MessageService messageService;

    /**
     * Endpoint for creating a new message.
     *
     * @param messageDto the message data transfer object containing the necessary information to create a new message
     * @return HTTP 200 OK with a standard response indicating the success of the message creation
     */
    @PostMapping
    public ResponseEntity<StandardResponseDto> createMessage(@RequestBody MessageCreateRequestDto messageDto) {
        StandardResponseDto response = messageService.createMessage(messageDto);
        return ResponseEntity.ok(response);
    }
}
