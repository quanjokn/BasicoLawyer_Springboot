package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    List<Post> findAll();
    Optional<Post> findById(UUID id);
    Post save(Post post);
    Post update(UUID id, Post post);
    void deleteById(UUID id);
}
