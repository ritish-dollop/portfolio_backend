package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ADMIN_CONTACT_MESSAGES)
@RequiredArgsConstructor
public class AdminContactMessageController {

    private final ContactMessageService contactMessageService;

    @GetMapping(ApiConstants.GET_MESSAGES)
    public ResponseEntity<ApiResponse<PagedResponse<ContactMessageResponse>>> getMessages(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY_CREATED_AT) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION_DESC) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(AppConstants.DEFAULT_SORT_DIRECTION_ASC)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<ContactMessageResponse> response = contactMessageService.getMessages(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping(ApiConstants.GET_UNREAD_COUNT)
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        long count = contactMessageService.countUnreadMessages();
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @GetMapping(ApiConstants.GET_MESSAGE_BY_ID)
    public ResponseEntity<ApiResponse<ContactMessageResponse>> getMessageById(@PathVariable String id) {
        ContactMessageResponse response = contactMessageService.getMessageById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping(ApiConstants.MARK_AS_READ)
    public ResponseEntity<ApiResponse<ContactMessageResponse>> markAsRead(
            @PathVariable String id,
            @RequestParam boolean read) {
        ContactMessageResponse response = contactMessageService.markAsRead(id, read);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_CONTACT_MESSAGE_STATUS_UPDATED, response));
    }

    @DeleteMapping(ApiConstants.DELETE_MESSAGE)
    public ResponseEntity<Void> deleteMessage(@PathVariable String id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
