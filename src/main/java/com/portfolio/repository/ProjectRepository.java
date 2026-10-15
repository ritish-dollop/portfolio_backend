package com.portfolio.repository;

import com.portfolio.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    Optional<Project> findBySlug(String slug);

    Page<Project> findAllByPublishedTrue(Pageable pageable);

    Optional<Project> findByPublishedTrueAndSlug(String slug);

    List<Project> findByFeaturedTrueAndPublishedTrueOrderByDisplayOrderAsc();

    boolean existsBySlugIgnoreCase(String slug);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, String id);

    Page<Project> findAllByOrderByDisplayOrderAsc(Pageable pageable);
}
