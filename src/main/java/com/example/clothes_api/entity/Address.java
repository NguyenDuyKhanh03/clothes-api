package com.example.clothes_api.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Ward ward;

    private String street;

    @ManyToMany(mappedBy = "addresses")
    private List<Account> accounts;
}
