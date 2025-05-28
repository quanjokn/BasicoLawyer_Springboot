package com.fptu.hk7.basicolawer.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // Bài viết liên quan đến danh mục này
}
