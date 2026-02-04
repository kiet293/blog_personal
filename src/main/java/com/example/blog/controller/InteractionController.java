package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class InteractionController {
    @Autowired private PostService postService;
    @Autowired private UserService userService;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentService commentService;

    // API like
    @PostMapping("/api/post/{id}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleLikeApi(@PathVariable("id") Long postId, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        // Kiem tra nguoi dung da dang nhap chua
        if (principal == null) {
            response.put("success", false);
            return ResponseEntity.ok(response);
        }

        User user = userService.findByUsername(principal.getName());
        Post post = postService.getPostById(postId);

        boolean isLiked = false;
        if (post != null) {
            if (post.getLikes().contains(user)) {
                post.getLikes().remove(user); // Unlike
                isLiked = false;
            } else {
                post.getLikes().add(user); // Like
                isLiked = true;
            }
            postRepository.save(post);

            response.put("success", true);
            response.put("liked", isLiked);
            response.put("likeCount", post.getLikes().size());
        }
        return ResponseEntity.ok(response);
    }

    // Xu ly comment
    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable("id") Long postId,
                             @RequestParam("content") String content,
                             Principal principal) {

        if (principal != null && content != null && !content.trim().isEmpty()) {
            User user = userService.findByUsername(principal.getName());
            Post post = postService.getPostById(postId);

            if (post != null) {
                commentService.addComment(content, user, post);
            }
        }
        // Quay lại trang chi tiết bài viết
        return "redirect:/post/" + postId;
    }
}
