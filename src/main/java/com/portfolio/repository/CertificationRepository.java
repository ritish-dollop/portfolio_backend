package com.portfolio.repository;

import com.portfolio.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {

    List<Certification> findAllByOrderByIssueDateDesc();
}
