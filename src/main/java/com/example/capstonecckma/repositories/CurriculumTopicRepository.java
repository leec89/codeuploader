package com.example.capstonecckma.repositories;

import com.example.capstonecckma.model.CurriculumTopic;
import com.example.capstonecckma.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumTopicRepository extends JpaRepository<CurriculumTopic, Long> {
    CurriculumTopic findAllById(long id);
    CurriculumTopic findByTitle(String title);
}
