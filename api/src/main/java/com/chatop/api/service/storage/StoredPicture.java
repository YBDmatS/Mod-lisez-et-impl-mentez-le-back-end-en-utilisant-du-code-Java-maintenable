package com.chatop.api.service.storage;

/**
 * Represents a stored picture with its associated metadata
 *
 * @param uuid        Unique identifier for the stored picture
 * @param relativeUrl Relative URL for accessing the stored picture
 * @param fileName    Original file name of the stored picture
 * @param storagePath Storage path where the picture is saved on the server
 */
public record StoredPicture(
        String uuid,
        String relativeUrl,
        String fileName,
        String storagePath
) {
}
