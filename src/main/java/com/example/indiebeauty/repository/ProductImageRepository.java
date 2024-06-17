package com.example.indiebeauty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.indiebeauty.domain.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

}
