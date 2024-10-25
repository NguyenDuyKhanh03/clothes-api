package com.example.clothes_api.entity;

import com.example.clothes_api.enumEntity.Size;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products",indexes = {
        @Index(name = "product_name_index", columnList = "name"),
        @Index(name = "product_description_index", columnList = "description")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String description;
    private Double price;
    private int quantity;

    @Column(nullable = true)
    private double height;
    @Column(nullable = true)
    private double width;
    @Column(nullable = true)
    private double weight;
    @Column(nullable = true)
    private double length;

    @Column(name = "sold_quantity")
    private double soldQuantity;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<Image> images = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();

    @CreatedDate
    @Column(name = "create_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "update_at")
    private Date updatedAt;

    @ElementCollection(targetClass = Size.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "product_size", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<Size> sizes=new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Color> colors=new ArrayList<>();

}
