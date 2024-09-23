package com.example.clothes_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private int quantity;
    private String color;
    private String size;

    @Column(name = "sold_quantity")
    private double soldQuantity;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<Image> images = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();

    @CreatedDate
    @Column(name = "create_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "update_at")
    private Date updatedAt;


}
