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
@Table(name = "form")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Form {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String category;
    private Float price;
    private String content;
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    private List<UserForm> userForms;
}
