package com.example.capstonecckma.repositories;

import com.example.capstonecckma.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository <Resource, Long> {

    Resource findByTitle(String title);
}
