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

/**
 * Service responsible for handling picture storage operations, including validation, saving, and deletion of pictures.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PictureStorageService {

    private final PictureStorageProperties properties;

    /**
     * Stores a picture
     *
     * @param picture
     * @return a StoredPicture object containing metadata about the stored picture
     */
    public StoredPicture storePicture(MultipartFile picture) {

        validateNotEmpty(picture);

        String extension = validateExtension(picture);
        validateMimeType(picture);

        String uuid = UUID.randomUUID().toString();
        String fileName = buildFileName(uuid, extension);

        Path storagePath = resolveStoragePath(fileName);
        ensureDirectoryExists(storagePath);

        saveFile(picture, storagePath);

        String url = buildPublicUrl(fileName);

        return new StoredPicture(
                uuid,
                url,
                fileName,
                storagePath.toAbsolutePath().toString()
        );
    }

    /**
     * Validates that the provided picture is not empty. If the picture is empty, an IllegalArgumentException is thrown.
     *
     * @param picture The MultipartFile picture to validate
     */
    private void validateNotEmpty(MultipartFile picture) {
        if (picture.isEmpty()) {
            log.error("Empty picture for picture: {}", picture.getOriginalFilename());
            throw new IllegalArgumentException("Picture must not be empty");
        }
    }

    /**
     * Validates the file extension of the provided picture. If the extension is not allowed,
     * an IllegalArgumentException is thrown.
     *
     * @param picture The MultipartFile picture to validate
     * @return the validated file extension in lowercase
     */
    private String validateExtension(MultipartFile picture) {
        String extension = extractExtension(picture);

        List<String> allowed = List.of("jpg", "jpeg", "png", "webp");

        if (!allowed.contains(extension)) {
            log.error("Invalid extension {} for picture {}", extension, picture.getOriginalFilename());
            throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }
        return extension;
    }

    /**
     * Extracts the file extension from the original filename of the provided picture. If the filename does not have a valid extension,
     * an IllegalArgumentException is thrown.
     *
     * @param picture The MultipartFile picture from which to extract the file extension
     * @return the extracted file extension in lowercase
     */
    private String extractExtension(MultipartFile picture) {
        String extension = StringUtils.getFilenameExtension(picture.getOriginalFilename());
        if (extension == null || extension.isBlank()) {
            log.error("Empty extension for picture: {}", picture.getOriginalFilename());
            throw new IllegalArgumentException("File must have a valid extension");
        }
        return extension.toLowerCase();
    }

    /**
     * Validates the MIME type of the provided picture. If the MIME type is not allowed,
     * an IllegalArgumentException is thrown.
     *
     * @param picture The MultipartFile picture to validate
     */
    private void validateMimeType(MultipartFile picture) {
        String contentType = picture.getContentType();

        List<String> allowed = List.of("image/jpeg", "image/png", "image/webp");

        if (contentType == null || !allowed.contains(contentType)) {
            log.error("Invalid content type {} for picture: {}", contentType, picture.getOriginalFilename());
            throw new IllegalArgumentException("Unsupported MIME type: " + contentType);
        }
    }

    /**
     * Builds a unique file name for the picture using a UUID and the validated file extension.
     *
     * @param uuid      The generated UUID for the picture
     * @param extension The validated file extension of the picture
     * @return A string representing the unique file name for the picture
     */
    private String buildFileName(String uuid, String extension) {
        return uuid + "." + extension;
    }

    /**
     * Resolves the storage path for the picture based on the configured upload directory and the generated file name.
     *
     * @param fileName The generated file name for the picture
     * @return A Path object representing the resolved storage path for the picture
     */
    private Path resolveStoragePath(String fileName) {
        Path uploadDir = Paths.get(properties.getUploadDir());
        return uploadDir.resolve(fileName);
    }

    /**
     * Ensures that the directory for the provided storage path exists. If the directory does not exist, it is created.
     * If the directory cannot be created, an IllegalStateException is thrown.
     *
     * @param path The Path object representing the storage path for the picture
     */
    private void ensureDirectoryExists(Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            log.error("Unable to create directory {}", path);
            throw new IllegalStateException("Failed to create storage directory", e);
        }
    }

    /**
     * Saves the provided picture to the specified storage path. If the file cannot be saved,
     * an IllegalStateException is thrown.
     *
     * @param picture The MultipartFile picture to be saved
     * @param path    The Path object representing the storage path where the picture should be saved
     */
    private void saveFile(MultipartFile picture, Path path) {
        try {
            picture.transferTo(path);
        } catch (IOException e) {
            log.error("Unable to save picture {} in {}", picture.getOriginalFilename(), path);
            throw new IllegalStateException("Failed to store file", e);
        }
    }

    /**
     * Builds the public URL for accessing the stored picture based on the configured relative public URL and the generated file name.
     *
     * @param fileName The generated file name for the picture
     * @return A string representing the public URL for accessing the stored picture
     */
    private String buildPublicUrl(String fileName) {
        return properties.getRelativePublicUrl() + fileName;
    }

    /**
     * Deletes the picture from storage based on the provided StoredPicture object. If the file cannot be deleted,
     * an IllegalStateException is thrown.
     *
     * @param picture The StoredPicture object containing metadata about the picture to be deleted
     */
    public void deleteByStoragePath(StoredPicture picture) {
        try {
            Path picturePath = Path.of(picture.storagePath());
            Files.deleteIfExists(picturePath);
        } catch (Exception e) {
            log.error("Failed to delete picture {} from storage: {}", picture.fileName(), picture.storagePath());
            throw new IllegalStateException("Failed to delete picture from storage", e);
        }
    }
}
