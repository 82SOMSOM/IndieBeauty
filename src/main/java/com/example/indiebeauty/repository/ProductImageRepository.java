package com.example.indiebeauty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

}
