package com.portfolio.service;

import com.portfolio.dto.response.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {

    ImageUploadResponse uploadImage(MultipartFile file, String folder);

    Map<String, Object> deleteImage(String publicId);
}
