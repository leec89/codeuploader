package com.example.codeuploader.repositories;

import com.example.codeuploader.model.CurriculumTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumTopicRepository extends JpaRepository<CurriculumTopic, Long> {
    CurriculumTopic findAllById(long id);
    CurriculumTopic findByTitle(String title);

    CurriculumTopic findById(long id);
}
