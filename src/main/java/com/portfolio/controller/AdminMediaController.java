package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ImageUploadResponse;
import com.portfolio.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(ApiConstants.ADMIN_MEDIA)
@RequiredArgsConstructor
public class AdminMediaController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = ApiConstants.UPLOAD_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = AppConstants.DEFAULT_CLOUDINARY_FOLDER) String folder) {
        ImageUploadResponse response = cloudinaryService.uploadImage(file, folder);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_IMAGE_UPLOADED_SUCCESS, response));
    }

    @DeleteMapping(ApiConstants.DELETE_IMAGE + "/{publicId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteImage(@PathVariable String publicId) {
        Map<String, Object> result = cloudinaryService.deleteImage(publicId);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_IMAGE_DELETED_SUCCESS, result));
    }
}
