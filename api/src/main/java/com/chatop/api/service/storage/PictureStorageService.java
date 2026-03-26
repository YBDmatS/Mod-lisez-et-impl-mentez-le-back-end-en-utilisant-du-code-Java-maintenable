package com.chatop.api.service.storage;

import com.chatop.api.config.properties.PictureStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PictureStorageService {

    private final PictureStorageProperties properties;

    public StoredPicture storePicture(MultipartFile picture) {
        log.debug("Storing picture");

        validateNotEmpty(picture);

        String extension = extractExtension(picture);

        validateExtension(extension);
        validateMimeType(picture);

        String uuid = UUID.randomUUID().toString();
        String fileName = buildFileName(uuid, extension);

        Path storagePath = resolveStoragePath(fileName);
        ensureDirectoryExists(storagePath);

        saveFile(picture, storagePath);

        String url = buildPublicUrl(fileName);

        log.debug("Picture stored successfully: {}", fileName);

        return new StoredPicture(
                uuid,
                url,
                fileName,
                storagePath.toAbsolutePath().toString()
        );
    }

    private void validateNotEmpty(MultipartFile picture) {
        if (picture.isEmpty()) {
            throw new IllegalArgumentException("Picture must not be empty");
        }
    }


    private String extractExtension(MultipartFile picture) {
        String extension = StringUtils.getFilenameExtension(picture.getOriginalFilename());
        if (extension == null || extension.isBlank()) {
            throw new IllegalArgumentException("File must have a valid extension");
        }
        return extension.toLowerCase();
    }

    private void validateExtension(String extension) {
        List<String> allowed = List.of("jpg", "jpeg", "png", "webp");

        if (!allowed.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }
    }

    private void validateMimeType(MultipartFile picture) {
        String contentType = picture.getContentType();

        List<String> allowed = List.of("image/jpeg", "image/png", "image/webp");

        if (contentType == null || !allowed.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported MIME type: " + contentType);
        }
    }

    private String buildFileName(String uuid, String extension) {
        return uuid + "." + extension;
    }

    private Path resolveStoragePath(String fileName) {
        Path uploadDir = Paths.get(properties.getUploadDir());
        return uploadDir.resolve(fileName);
    }

    private void ensureDirectoryExists(Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create storage directory", e);
        }
    }

    private void saveFile(MultipartFile picture, Path path) {
        try {
            picture.transferTo(path);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file", e);
        }
    }

    private String buildPublicUrl(String fileName) {
        return properties.getRelativePublicUrl() + fileName;
    }

    public void deleteByStoragePath(String path) {
        try {
            Path picturePath = Path.of(path);
            Files.deleteIfExists(picturePath);
            log.debug("Deleted picture from storage: {}", path);
        } catch (Exception e) {
            log.error("Failed to delete picture from storage: {}", path, e);
            throw new IllegalStateException("Failed to delete picture from storage", e);
        }
    }
}
