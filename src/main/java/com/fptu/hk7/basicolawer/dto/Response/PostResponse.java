package com.fptu.hk7.basicolawer.dto.Response;

import com.fptu.hk7.basicolawer.pojo.Category;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.UUID;

@Data
public class PostResponse {
    private UUID id;

    private String topic;

    private String htmlContent;

    private String deltaContent;

    private String stamp;

    private int view;

    private String thumbnail;

    private String category_name;
}
