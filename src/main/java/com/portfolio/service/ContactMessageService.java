package com.portfolio.service;

import com.portfolio.dto.request.ContactMessageRequest;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ContactMessageService {

    ContactMessageResponse submitMessage(ContactMessageRequest request);

    PagedResponse<ContactMessageResponse> getMessages(Pageable pageable);

    ContactMessageResponse getMessageById(String id);

    ContactMessageResponse markAsRead(String id, boolean read);

    void deleteMessage(String id);

    long countUnreadMessages();
}
