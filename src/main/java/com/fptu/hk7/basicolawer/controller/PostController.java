package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.PostRequest;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import com.fptu.hk7.basicolawer.pojo.Category;
import com.fptu.hk7.basicolawer.pojo.Post;
import com.fptu.hk7.basicolawer.service.CategoryService;
import com.fptu.hk7.basicolawer.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final CategoryService categoryService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public List<Post> getAll() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable UUID id) {
        Optional<Post> post = postService.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PreAuthorize("hasRole(ADMIN)")
    @PostMapping("/create")
    public Post create(@RequestBody PostRequest postRequest) {
        Post post = modelMapper.map(postRequest, Post.class);
        Category category = categoryService.findById(UUID.fromString(postRequest.getCategoryId())).orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY));
        post.setCategory(category);
        post.setLiked(0);
        return postService.save(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable UUID id, @RequestBody Post post) {
        try {
            return ResponseEntity.ok(postService.update(id, post));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
