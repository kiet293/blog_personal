package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Công cụ mã hóa lấy từ SecurityConfig

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public void registerUser(User user) {
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Gan vai trò mặc định là "USER"
        user.setRole("USER");

        // Lưu user vào database
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Tìm user theo ID (Cần dùng khi bấm nút Follow ai đó)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Hàm lưu User chung (dùng khi cần save thủ công)
    public void save(User user) {
        userRepository.save(user);
    }

    public void updateProfile (User user, String fullName, String bio, MultipartFile avatarFile) {
        user.setFullName(fullName);
        user.setBio(bio);

        // Xử lý ảnh đại diện nếu có
        if (!avatarFile.isEmpty()) {
            try {
                String fileName = java.util.UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename();
                java.nio.file.Path path = java.nio.file.Paths.get(UPLOAD_DIR + fileName);

                // Kiểm tra thư mục uploads có tồn tại chưa, chưa thì tạo
                if (!Files.exists(Paths.get(UPLOAD_DIR))) {
                    Files.createDirectories(Paths.get(UPLOAD_DIR));
                }

                // Lưu file
                Files.copy(avatarFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Lưu tên file vào Database
                user.setAvatar(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userRepository.save(user);
    }

    @Transactional
    public void toggleFollow(User currentUser, User targetUser) {
        if (currentUser.getFollowing().contains(targetUser)) {
            // Nếu đã theo dõi, bỏ theo dõi
            currentUser.getFollowing().remove(targetUser);
            targetUser.getFollowers().remove(currentUser);
        } else {
            // Nếu chưa theo dõi, thêm theo dõi
            currentUser.getFollowing().add(targetUser);
            targetUser.getFollowers().add(currentUser);
        }
        // Lưu thay đổi vào database
        userRepository.save(currentUser);
    }

}
