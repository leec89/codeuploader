package com.example.codeuploader.repositories;

import com.example.codeuploader.model.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRepository extends JpaRepository<Doc, Integer> {

}
