package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	// 전체 상품 조회 (페이징 처리)
	Page<Product> findAll(Pageable pageable);
	
	// categoryId로 상품 검색 (페이징 처리)
	Page<Product> findByCategory_CategoryId(int categoryId, Pageable pageable);
	
	// 키워드로 상품 검색 (상품 이름 기준)
	List<Product> findByNameContainingIgnoreCase(String keyword);
	
	// 키워드로 상품 검색 (카테고리 이름 기준)
	List<Product> findByCategory_NameContainingIgnoreCase(String keyword);
	
	// 설명에 포함된 상품 검색 (상품 설명 기준)
	List<Product> findByDescriptionContainingIgnoreCase(String keyword);
	
	boolean existsByProductIdAndStockGreaterThan(int productId, int stock);
	
}