package com.example.blog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Sẽ được mã hóa, không lưu text thường

    private String fullName;
    private String email;
    private String role; // Vai trò người dùng (ví dụ: USER, ADMIN)

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private java.util.List<Post> posts; // Danh sách bài viết của người dùng

    // Constructor rỗng
    public User() {}

    // Getters và Setters (Bắt buộc phải có đủ)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
