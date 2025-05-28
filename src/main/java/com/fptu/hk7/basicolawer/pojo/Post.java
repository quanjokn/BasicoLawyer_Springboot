package com.fptu.hk7.basicolawer.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String topic;
    private String content;
    private String stamp;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người đăng bài viết

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Category> categories;
}
