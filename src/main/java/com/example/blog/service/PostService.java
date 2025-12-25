package com.example.blog.service;

import com.example.blog.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service // Đánh dấu đây là nơi xử lý logic nghiệp vụ
public class PostService {

    // 1. Khai báo List ở ngoài để nó tồn tại xuyên suốt vòng đời ứng dụng
    private List<Post> posts = new ArrayList<>();

    // 2. Constructor: Tạo sẵn vài bài viết mẫu khi khởi động ứng dụng
    public PostService() {
        posts.add(new Post("Bài viết khởi tạo 1", "Nội dung mẫu 1...", "25/12/2025"));
        posts.add(new Post("Bài viết khởi tạo 2", "Nội dung mẫu 2...", "26/12/2025"));
    }

    public void addPost(Post newPost) {
        posts.add(newPost);
    }

    public List<Post> getAllPosts() {
        return posts;
    }
}
