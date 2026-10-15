package com.portfolio.service.impl;

import com.portfolio.dto.request.ContactMessageRequest;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.entity.ContactMessage;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ContactMessageMapper;
import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    @Override
    @Transactional
    public ContactMessageResponse submitMessage(ContactMessageRequest request) {
        ContactMessage message = contactMessageMapper.toEntity(request);
        ContactMessage saved = contactMessageRepository.save(message);
        log.info("Received new contact message from: {} (ID: {})", saved.getEmail(), saved.getId());
        return contactMessageMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ContactMessageResponse> getMessages(Pageable pageable) {
        Page<ContactMessage> page = contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PagedResponse.of(page.map(contactMessageMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessageResponse getMessageById(String id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", "id", id));
        return contactMessageMapper.toResponse(message);
    }

    @Override
    @Transactional
    public ContactMessageResponse markAsRead(String id, boolean read) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", "id", id));

        message.setRead(read);
        ContactMessage updated = contactMessageRepository.save(message);
        log.info("Updated read status to {} for message ID: {}", read, id);
        return contactMessageMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteMessage(String id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", "id", id));
        contactMessageRepository.delete(message);
        log.info("Deleted contact message with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadMessages() {
        return contactMessageRepository.countByReadFalse();
    }
}
