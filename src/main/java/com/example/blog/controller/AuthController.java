package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    // display form register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // xu ly khi user nhan dang ky
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result, // biến này chứa kết quả check lỗi
                               Model model) {
        // check xem user đã tồn tại chưa
        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "error.user", "Tên đăng nhập này đã được sử dụng");
        }

        // nếu có lỗi, trả về trang đăng ký với thông báo lỗi
        if (result.hasErrors()) {
            return "register"; // Trả về trang đăng ký để hiện lỗi
        }
        // nếu không có lỗi, lưu user vào database
        userService.registerUser(user);
        return "redirect:/login?success"; // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
    }

    // display form login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
