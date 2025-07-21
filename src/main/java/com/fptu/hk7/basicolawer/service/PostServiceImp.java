package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicoeventdata.event.PostEvent;
import com.fptu.hk7.basicolawer.dto.Request.PostRequest;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import com.fptu.hk7.basicolawer.pojo.Post;
import com.fptu.hk7.basicolawer.producer.PostProducer;
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
    private final ModelMapper modelMapper;
    private final PostProducer postProducer;

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
        Post p = postRepository.save(post);
        //sync
        PostEvent event = modelMapper.map(p, PostEvent.class);
        event.setCategories(p.getCategory().getName());
        postProducer.produce(event, "post-create");
        return p;
    }

    @Override
    public Post update(UUID id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_POST));
        post.setCategory(categoryRepository.findById(UUID.fromString(postRequest.getCategoryId())).orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY)));
        post.setTopic(postRequest.getTopic());
        post.setDeltaContent(postRequest.getDeltaContent());
        post.setHtmlContent(postRequest.getHtmlContent());
        post.setThumbnail(postRequest.getThumbnail());

        postRepository.save(post);
        //sync
        PostEvent event = modelMapper.map(post, PostEvent.class);
        event.setCategories(post.getCategory().getName());
        postProducer.produce(event, "post-update");
        return post;
    }

    @Override
    public void deleteById(UUID id) {
        postRepository.deleteById(id);
        // sync
        PostEvent event = new PostEvent();
        event.setId(id);
        postProducer.produce(event, "post-delete");
    }

    @Override
    public void readPost(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_POST));
        post.setView(post.getView() + 1);
        save(post);
    }
}
