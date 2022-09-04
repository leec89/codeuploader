package com.example.codeuploader.repositories;

import com.example.codeuploader.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByResourceId(Long resourceId, Pageable pageable);
    Optional<Comment> findByIdAndResourceId(Long id, Long postId);

    Page<Comment> findAll(Pageable pageable);
}
