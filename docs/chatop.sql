CREATE DATABASE IF NOT EXISTS `chatop`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE chatop;

CREATE TABLE IF NOT EXISTS `chatop`.`users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL, 
    `email` varchar(255) NOT NULL, 
    `password_hash` varchar(255) NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `chatop`.`rentals` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `surface_area` DECIMAL(10,2) NOT NULL,
    `price_per_night` DECIMAL(10,2) NOT NULL,
    `picture_url` varchar(255) NOT NULL,
    `description` text NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_rentals_owner` FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `chatop`.`messages` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `sender_id` BIGINT NOT NULL,
    `rental_id` BIGINT NOT NULL,
    `content` text NOT NULL,
    `sent_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users`(`id`),
    CONSTRAINT `fk_messages_rental` FOREIGN KEY (`rental_id`) REFERENCES `rentals`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;