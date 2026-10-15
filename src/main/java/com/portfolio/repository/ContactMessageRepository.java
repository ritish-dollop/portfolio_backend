package com.portfolio.repository;

import com.portfolio.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, String> {

    Page<ContactMessage> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByReadFalse();
}
