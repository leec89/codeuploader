package com.example.codeuploader.controllers;

import com.example.codeuploader.model.Comment;
import com.example.codeuploader.model.Resource;
import com.example.codeuploader.repositories.CommentRepository;
import com.example.codeuploader.repositories.ResourceRepository;
import com.example.codeuploader.rest.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@RestController
@Controller
public class CommentController {



    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public CommentController(CommentRepository commentRepository, ResourceRepository resourceRepository) {
        this.commentRepository = commentRepository;
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/resources/{resourcesId}/comments")
//    public Page<Comment> getAllCommentsByPostId(@PathVariable(value = "resourcesId") Long resourcesId,
//                                                Pageable pageable, Model vModel) {
//         commentRepository.findByResourceId(resourcesId, pageable);
//        return commentRepository.findByResourceId(resourcesId, pageable);
//    public String viewAllComments(@PathVariable(value = "resourcesId") Long resourcesId, Model vModel, @PageableDefault(value=10)  Pageable pageable) {
//        vModel.addAttribute("page", commentRepository.findByResourceId(resourcesId, pageable) );
    public String viewAllComments(Model vModel, @PageableDefault(value=10) Pageable pageable, @PathVariable(value = "resourcesId") Long resourcesId){
        Page<Comment> comments = commentRepository.findAll(pageable);
        Page<Comment> comments1 = commentRepository.findByResourceId(resourcesId, pageable);
        vModel.addAttribute("page", comments );
        return "page";
    }

    @PostMapping("/resources/{resourceId}/comments")
    public String createComment(@PathVariable (value = "resourceId") long resourceId, @ModelAttribute Comment comment) {
//        return resourceRepository.findById(resourceId).map(resource -> {
//            comment.setResource(resource);
//            return commentRepository.save(comment);
//        }).orElseThrow(() -> new ResourceNotFoundException("ResourceId " + resourceId + " not found"));
      Resource resource = resourceRepository.findById(resourceId).get();
      comment.setResource(resource);
      commentRepository.save(comment);
      return "redirect:/resources/" + resourceId;

    }

    @PutMapping("/resources/{resourcesId}/comments/{commentId}")
    public Comment updateComment(@PathVariable (value = "resourceId") Long resourcesId,
                                 @PathVariable (value = "commentId") Long commentId,
                                 @Valid @RequestBody Comment commentRequest) {
        if(!resourceRepository.existsById(resourcesId)) {
            throw new ResourceNotFoundException("PostId " + resourcesId + " not found");
        }

        return commentRepository.findById(commentId).map(comment -> {
            comment.setText(commentRequest.getText());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + "not found"));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "resourceId") Long resourcetId,
                                           @PathVariable (value = "commentId") Long commentId) {
        return commentRepository.findByIdAndResourceId(commentId, resourcetId).map(comment -> {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId + " and postId " + resourcetId));
    }
}
