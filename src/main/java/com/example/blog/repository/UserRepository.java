package com.example.blog.repository;

import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm user theo tên đăng nhập
    User findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(String username, String fullName);
}
