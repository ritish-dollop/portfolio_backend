package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.ContactMessageRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.PUBLIC_CONTACT)
@RequiredArgsConstructor
public class PublicContactController {

    private final ContactMessageService contactMessageService;

    @PostMapping(ApiConstants.SUBMIT_CONTACT_MESSAGE)
    public ResponseEntity<ApiResponse<ContactMessageResponse>> submitContactMessage(
            @Valid @RequestBody ContactMessageRequest request) {
        ContactMessageResponse response = contactMessageService.submitMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_CONTACT_MESSAGE_SUBMIT_SUCCESS, response));
    }
}
