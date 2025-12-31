package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    // save comment
    public void addComment(String content, User user, Post post) {
        com.example.blog.model.Comment comment = new com.example.blog.model.Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm");
        comment.setDate(java.time.LocalDateTime.now().format(formatter));

        commentRepository.save(comment);
    }
}
