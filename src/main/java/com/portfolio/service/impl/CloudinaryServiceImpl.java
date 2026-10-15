package com.portfolio.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.portfolio.constant.AppConstants;
import com.portfolio.constant.ValidationMessageConstants;
import com.portfolio.dto.response.ImageUploadResponse;
import com.portfolio.exception.BadRequestException;
import com.portfolio.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp",
            "image/gif",
            "image/svg+xml"
    );

    @Override
    public ImageUploadResponse uploadImage(MultipartFile file, String folder) {
        validateFile(file);

        String targetFolder = StringUtils.hasText(folder) ? folder.trim() : AppConstants.DEFAULT_CLOUDINARY_FOLDER;

        try {
            Map<?, ?> uploadParams = ObjectUtils.asMap(
                    "folder", targetFolder,
                    "resource_type", "auto"
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), uploadParams);

            log.info("Successfully uploaded image to Cloudinary: public_id={}", uploadResult.get("public_id"));

            return ImageUploadResponse.builder()
                    .publicId((String) uploadResult.get("public_id"))
                    .url((String) uploadResult.get("url"))
                    .secureUrl((String) uploadResult.get("secure_url"))
                    .format((String) uploadResult.get("format"))
                    .width(uploadResult.get("width") != null ? ((Number) uploadResult.get("width")).intValue() : null)
                    .height(uploadResult.get("height") != null ? ((Number) uploadResult.get("height")).intValue() : null)
                    .bytes(uploadResult.get("bytes") != null ? ((Number) uploadResult.get("bytes")).longValue() : null)
                    .originalFilename(file.getOriginalFilename())
                    .build();
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new BadRequestException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> deleteImage(String publicId) {
        if (!StringUtils.hasText(publicId)) {
            throw new BadRequestException(ValidationMessageConstants.PUBLIC_ID_REQUIRED);
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().destroy(publicId.trim(), ObjectUtils.emptyMap());
            log.info("Deleted image from Cloudinary: public_id={}, result={}", publicId, result.get("result"));
            return result;
        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary with publicId: {}", publicId, e);
            throw new BadRequestException("Failed to delete image: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException(ValidationMessageConstants.FILE_EMPTY);
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException(ValidationMessageConstants.FILE_NOT_IMAGE);
        }
    }
}
