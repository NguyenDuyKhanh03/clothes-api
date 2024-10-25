package com.example.clothes_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "ward_id")
    private Integer wardId;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;
}
