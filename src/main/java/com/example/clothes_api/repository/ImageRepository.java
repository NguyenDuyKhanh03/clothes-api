package com.example.clothes_api.repository;

import com.example.clothes_api.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
