package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service // Đánh dấu đây là nơi xử lý logic nghiệp vụ
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Add bài viết mới
    public void addPost(Post post) {
        postRepository.save(post);
    }

    // Lấy tất cả bài viết
    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // Hàm xóa bài viết theo ID
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Hàm lấy bài viết theo tác giả
    public List<Post> getPostsByAuthor(User author) {
        return postRepository.findByAuthor(author);
    }

    // Hàm lấy bài viết theo ID
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

}
