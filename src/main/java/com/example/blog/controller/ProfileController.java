package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/profile/{username}")
    public String showProfile(@PathVariable String username, Model model) {
        // Tìm thông tin người dùng
        User user = userService.findByUsername(username);

        // Nếu không tìm thấy user (ví dụ nhập bừa), có thể chuyển về trang lỗi (ở đây tạm về trang chủ)
        if (user == null) {
            return "redirect:/";
        }

        List<Post> userPosts = postService.getPostsByAuthor(user);
        model.addAttribute("userProfile", user); // thong tin user
        model.addAttribute("posts", userPosts); // thong tin bai viet cua user

        return "profile";
    }
}
