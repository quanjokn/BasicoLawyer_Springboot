package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.PostRequest;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Response.PostResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final CategoryService categoryService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public List<PostResponse> getAll() {
        List<Post> posts = postService.findAll();
        return posts.stream().map(post -> {
            PostResponse postResponse = modelMapper.map(post, PostResponse.class);
            postResponse.setCategory_name(post.getCategory().getName());
            return postResponse;
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable("id") UUID id) {
        Optional<Post> post = postService.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PreAuthorize("hasRole(ADMIN)")
    @PostMapping("/create")
    public Post create(@RequestBody PostRequest postRequest) {
        Post post = modelMapper.map(postRequest, Post.class);
        Category category = categoryService.findById(UUID.fromString(postRequest.getCategoryId())).orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY));
        post.setCategory(category);
        post.setView(0);
        return postService.save(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> update(@PathVariable("id") String id, @Validated @RequestBody PostRequest postRequest) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Update successfully",
                            200,
                            postService.update(UUID.fromString(id), postRequest)
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/read/{id}")
    public void readPost(@PathVariable("id") String postId){
        postService.readPost(UUID.fromString(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
