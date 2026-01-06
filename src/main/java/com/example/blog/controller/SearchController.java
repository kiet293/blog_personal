package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "keyword", required = false) String keyword,
                              Model model,
                              Principal principal) {
        List<User> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(keyword, keyword);
        } else {
            users = userRepository.findAll();
        }

        if (principal != null) {
            String currentUsername = principal.getName();
            users.removeIf(u -> u.getUsername().equals(currentUsername));
        }
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
