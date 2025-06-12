package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Post;
import com.fptu.hk7.basicolawer.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;

    public PostServiceImp(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post update(UUID id, Post post) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found");
        }
        post.setId(id);
        return postRepository.save(post);
    }

    @Override
    public void deleteById(UUID id) {
        postRepository.deleteById(id);
    }
}
