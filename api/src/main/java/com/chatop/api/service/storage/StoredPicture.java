package com.chatop.api.service.storage;

public record StoredPicture(
        String uuid,
        String url,
        String fileName,
        String storagePath
) {
}
