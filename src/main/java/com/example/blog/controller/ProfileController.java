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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    // Xem profile
    @GetMapping("/profile/{username}")
    public String showProfile(@PathVariable String username, Model model, Principal principal) {
        User profileOwner = userService.findByUsername(username);

        if (profileOwner == null) {
            return "redirect:/"; // Hoặc trang lỗi nếu user không tồn tại
        }

        boolean isOwner = false;
        boolean isFollowing = false;

        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            if (currentUser.getUsername().equals(username)) {
                isOwner = true;
            } else {
                isFollowing = currentUser.getFollowing().contains(profileOwner);
            }
        }

        model.addAttribute("userProfile", profileOwner); // thong tin user
        model.addAttribute("posts", profileOwner.getPosts()); // thong tin bai viet cua user
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isFollowing", isFollowing);


        model.addAttribute("postCount", profileOwner.getPosts().size());
        model.addAttribute("followerCount", profileOwner.getFollowers().size());
        model.addAttribute("followingCount", profileOwner.getFollowing().size());

        return "profile";
    }

    // xu ly update
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("bio") String bio,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());

        // 👇 GỌI SERVICE - CODE GỌN HƠN HẲN
        userService.updateProfile(user, fullName, bio, avatarFile);

        return "redirect:/profile/" + user.getUsername();
    }

    // Xử lý Follow
    @PostMapping("/profile/follow/{id}")
    public String followUser(@PathVariable("id") Long targetUserId, Principal principal) {
        if (principal == null) return "redirect:/login";

        User currentUser = userService.findByUsername(principal.getName());
        User targetUser = userService.findById(targetUserId);

        if (targetUser != null && !currentUser.equals(targetUser)) {
            userService.toggleFollow(currentUser, targetUser);
        }
        return "redirect:/profile/" + targetUser.getUsername();
    }
}
