package com.example.blog.model;

import jakarta.persistence.*;
import com.example.blog.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 5000) // Giới hạn độ dài nội dung 5000 ký tự
    private String content;

    @Column(name = "publish_date")
    private String date;

    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id") // Khóa ngoại liên kết đến bảng users
    private User author;

    // danh sach like
    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes = new HashSet<>();

    // danh sach comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Comment> comments;



    public Post() {

    }

    // Constructor
    public Post(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // Getter (Bắt buộc để HTML đọc được)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public Set<User> getLikes() { return likes; }
    public void setLikes(Set<User> likes) { this.likes = likes; }
}
