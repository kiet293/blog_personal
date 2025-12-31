package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;


@Controller
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("postList", postService.getAllPosts());
        model.addAttribute("newPost", new Post()); // Dữ liệu rỗng để điền từ form
        return "index";
    }

    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("newPost") Post newPost,
                          BindingResult result,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Principal principal,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("postList", postService.getAllPosts());
            return "index"; // Trả về trang chủ với lỗi
        }

        if (!imageFile.isEmpty()) {
            try {
                // Tạo tên file độc nhất (tránh trùng tên)
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                // Đường dẫn lưu file (thư mục "uploads" trong thư mục gốc của dự án)
                Path uploadPath = Paths.get("uploads");

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file vào thư mục
                try (var inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }

                // Lưu tên file vào đối tượng bài viết
                newPost.setImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            newPost.setAuthor(user);
            newPost.setDate(java.time.LocalDate.now().toString());
        }

        postService.addPost(newPost);
        return "redirect:/";
    }

    @PostMapping("/comment/add")
    public String addComment(@RequestParam("postId") Long postId,
                             @RequestParam("content") String content,
                             Principal principal) {
        if (principal != null && !content.trim().isEmpty()) {
            User user = userService.findByUsername(principal.getName());
            Post post = postService.getPostById(postId);

            if (post != null) {
                commentService.addComment(content, user, post);
            }
        }
        return "redirect:/post/" + postId;
    }

    @GetMapping("/delete/{id}")
    public  String deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return "redirect:/"; // Quay về trang chủ sau khi xóa bài viết
    }

    @GetMapping("/post/{id}")
    public String viewPostDetails(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);

        // nếu bài viết không tồn tại
        if (post == null) {
            return "redirect:/"; // Nếu không tìm thấy bài viết, quay về trang chủ
        }
        model.addAttribute("post", post);
        return "post-detail"; // Trả về trang chi tiết bài viết
    }

}
