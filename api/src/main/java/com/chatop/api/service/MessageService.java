package com.chatop.api.service;

import com.chatop.api.dto.MessageDto;
import com.chatop.api.dto.StandardResponseDto;
import com.chatop.api.model.Message;
import com.chatop.api.model.Rental;
import com.chatop.api.model.User;
import com.chatop.api.repository.MessageRepository;
import com.chatop.api.repository.RentalRepository;
import com.chatop.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service handling message-related operations, such as creating and sending messages between users regarding rentals.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    public final MessageRepository messageRepository;
    public final UserRepository userRepository;
    public final RentalRepository rentalRepository;

    /**
     * Creates a new message based on the provided message data transfer object, associating it with the sender and the rental.
     *
     * @param messageDto the message data transfer object containing the user ID of the sender, the rental ID, and the message content
     * @return a standard response data transfer object indicating the success of the message creation
     */
    public StandardResponseDto createMessage(MessageDto messageDto) {
        Message message = new Message();

        User sender = userRepository.findById(messageDto.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", messageDto.getUserId());
                    return new RuntimeException("User not found with id: " + messageDto.getUserId());
                });
        message.setSender(sender);

        Rental rental = rentalRepository.findById(messageDto.getRentalId())
                .orElseThrow(() -> {
                    log.error("Rental not found with id: {}", messageDto.getRentalId());
                    return new RuntimeException("Rental not found with id: " + messageDto.getRentalId());
                });
        message.setRental(rental);

        message.setContent(messageDto.getMessage());

        messageRepository.save(message);
        return new StandardResponseDto("Message send with success");
    }
}
