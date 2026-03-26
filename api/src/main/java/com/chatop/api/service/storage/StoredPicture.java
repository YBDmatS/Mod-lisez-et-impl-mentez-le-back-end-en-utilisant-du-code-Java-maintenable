package com.chatop.api.service.storage;

public record StoredPicture(
        String uuid,
        String relativeUrl,
        String fileName,
        String storagePath
) {
}
