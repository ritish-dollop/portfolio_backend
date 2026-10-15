package com.portfolio.repository;

import com.portfolio.entity.Skill;
import com.portfolio.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, String> {

    List<Skill> findAllByActiveTrueOrderByDisplayOrderAsc();

    List<Skill> findByCategoryAndActiveTrueOrderByDisplayOrderAsc(SkillCategory category);

    List<Skill> findAllByOrderByDisplayOrderAsc();
}
