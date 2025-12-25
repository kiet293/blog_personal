package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import com.example.blog.model.User;
import com.example.blog.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Nguyễn Gia Kiệt"); // Dữ liệu từ ảnh cũ của bạn
        model.addAttribute("jobTitle", "Java Student");
        model.addAttribute("postList", postService.getAllPosts());
        model.addAttribute("newPost", new Post()); // Dữ liệu rỗng để điền từ form
        return "index";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Post newPost, Principal principal) {
        // Lấy thông tin user hiện tại từ Principal
        if (principal != null) {
            String username = principal.getName(); // Lấy username của user đã đăng nhập
            User user = userService.findByUsername(username); // Tìm user trong database
            newPost.setAuthor(user); // Gán tác giả cho bài viết
        }
        postService.addPost(newPost);
        return "redirect:/"; // Quay về trang chủ sau khi thêm bài viết
    }

    @GetMapping("/delete/{id}")
    public  String deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return "redirect:/"; // Quay về trang chủ sau khi xóa bài viết
    }
}
