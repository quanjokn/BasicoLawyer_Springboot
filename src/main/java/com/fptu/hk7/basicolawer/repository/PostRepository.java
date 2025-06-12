package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
