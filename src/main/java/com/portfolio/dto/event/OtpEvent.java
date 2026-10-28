package com.portfolio.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpEvent implements Serializable {

    private String email;
    private String otp;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
