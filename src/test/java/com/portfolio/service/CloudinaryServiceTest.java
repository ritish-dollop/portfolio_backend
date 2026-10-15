package com.portfolio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.portfolio.dto.response.ImageUploadResponse;
import com.portfolio.exception.BadRequestException;
import com.portfolio.service.impl.CloudinaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should successfully upload valid image file")
    void uploadImage_Success() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("public_id", "portfolio/test-image-123");
        mockResult.put("url", "http://res.cloudinary.com/demo/image/upload/v1/portfolio/test-image-123.jpg");
        mockResult.put("secure_url", "https://res.cloudinary.com/demo/image/upload/v1/portfolio/test-image-123.jpg");
        mockResult.put("format", "jpg");
        mockResult.put("width", 800);
        mockResult.put("height", 600);
        mockResult.put("bytes", 102400L);

        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(mockResult);

        MockMultipartFile file = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "dummy image content".getBytes()
        );

        ImageUploadResponse response = cloudinaryService.uploadImage(file, "portfolio");

        assertThat(response).isNotNull();
        assertThat(response.getPublicId()).isEqualTo("portfolio/test-image-123");
        assertThat(response.getSecureUrl()).startsWith("https://");
        assertThat(response.getFormat()).isEqualTo("jpg");
        assertThat(response.getWidth()).isEqualTo(800);
        assertThat(response.getHeight()).isEqualTo(600);
    }

    @Test
    @DisplayName("Should throw BadRequestException when file is empty")
    void uploadImage_EmptyFile_ThrowsBadRequestException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.jpg", "image/jpeg", new byte[0]
        );

        assertThatThrownBy(() -> cloudinaryService.uploadImage(emptyFile, "portfolio"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Should throw BadRequestException when file is not an image")
    void uploadImage_InvalidFileType_ThrowsBadRequestException() {
        MockMultipartFile textFile = new MockMultipartFile(
                "file", "document.pdf", "application/pdf", "some content".getBytes()
        );

        assertThatThrownBy(() -> cloudinaryService.uploadImage(textFile, "portfolio"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Should successfully delete image given valid publicId")
    void deleteImage_Success() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("result", "ok");

        when(uploader.destroy(eq("portfolio/test-image-123"), anyMap())).thenReturn(mockResult);

        Map<String, Object> result = cloudinaryService.deleteImage("portfolio/test-image-123");

        assertThat(result).isNotNull();
        assertThat(result.get("result")).isEqualTo("ok");
        verify(uploader).destroy(eq("portfolio/test-image-123"), anyMap());
    }

    @Test
    @DisplayName("Should throw BadRequestException when publicId is blank")
    void deleteImage_BlankPublicId_ThrowsBadRequestException() {
        assertThatThrownBy(() -> cloudinaryService.deleteImage("   "))
                .isInstanceOf(BadRequestException.class);
    }
}
