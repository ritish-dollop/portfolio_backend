package com.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {

    private String publicId;
    private String url;
    private String secureUrl;
    private String format;
    private Integer width;
    private Integer height;
    private Long bytes;
    private String originalFilename;
    private boolean progressive;
    private String progressiveUrl;
}
