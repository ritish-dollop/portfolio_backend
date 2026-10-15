package com.portfolio.repository;

import com.portfolio.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, String> {

    List<SocialLink> findAllByActiveTrueOrderByDisplayOrderAsc();

    List<SocialLink> findAllByOrderByDisplayOrderAsc();
}
