package com.chatop.api.service.storage;

/**
 * Represents a stored picture with its associated metadata
 *
 * @param uuid
 * @param relativeUrl
 * @param fileName
 * @param storagePath
 */
public record StoredPicture(
        String uuid,
        String relativeUrl,
        String fileName,
        String storagePath
) {
}
