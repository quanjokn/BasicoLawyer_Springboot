package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.PostRequest;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import com.fptu.hk7.basicolawer.pojo.Post;
import com.fptu.hk7.basicolawer.repository.CategoryRepository;
import com.fptu.hk7.basicolawer.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

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
    public Post update(UUID id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_POST));
        post.setCategory(categoryRepository.findById(UUID.fromString(postRequest.getCategoryId())).orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY)));
        post.setTopic(postRequest.getTopic());
        post.setDeltaContent(postRequest.getDeltaContent());
        post.setHtmlContent(postRequest.getHtmlContent());
        post.setThumbnail(postRequest.getThumbnail());
        return postRepository.save(post);
    }

    @Override
    public void deleteById(UUID id) {
        postRepository.deleteById(id);
    }

    @Override
    public void readPost(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_POST));
        post.setView(post.getView() + 1);
        postRepository.save(post);
    }
}
