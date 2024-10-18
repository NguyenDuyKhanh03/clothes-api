package com.example.clothes_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "provinces")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "province_id")
    private Integer provinceId;

}
