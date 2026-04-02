package com.chatop.api.controller;

import com.chatop.api.dto.MessageCreateRequestDto;
import com.chatop.api.dto.StandardResponseDto;
import com.chatop.api.exception.ApiErrorResponse;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
@Tag(name = "Messages", description = "Messaging endpoints for rental inquiries")
public class MessageController {

    public final MessageService messageService;

    /**
     * Endpoint for creating a new message.
     *
     * @param messageDto the message data transfer object containing the necessary information to create a new message
     * @return HTTP 200 OK with a standard response indicating the success of the message creation
     * @throws ResourceNotFoundException when the user or the rental no longer exists in the database (HTTP 404)
     */
    @PostMapping
    @Operation(summary = "Send a message", description = "Sends a message related to a rental listing from the authenticated user.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MessageCreateRequestDto.class)))
    @ApiResponse(responseCode = "200", description = "Message sent successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid message data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Sender or rental not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<StandardResponseDto> createMessage(@RequestBody MessageCreateRequestDto messageDto) {
        StandardResponseDto response = messageService.createMessage(messageDto);
        return ResponseEntity.ok(response);
    }
}
