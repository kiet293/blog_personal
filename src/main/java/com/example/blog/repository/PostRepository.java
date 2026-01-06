package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByAuthor(User author);

    List<Post> findByAuthorInOrderByIdDesc(Collection<User> authors);

    List<Post> findAllByOrderByIdDesc();
}
