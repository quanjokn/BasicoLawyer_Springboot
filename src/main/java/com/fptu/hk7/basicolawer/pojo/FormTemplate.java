package com.fptu.hk7.basicolawer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "form_template")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormTemplate {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String category;

    @Column(name = "json_structure", columnDefinition = "TEXT")
    private String jsonStructure; // định nghĩa cấu trúc form

    @Column(name = "html_template", columnDefinition = "TEXT")
    private String htmlTemplate;

    private String description;

    private String imgUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<UserForm> userForms;



}
