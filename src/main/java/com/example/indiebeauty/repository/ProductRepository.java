package com.example.indiebeauty.repository;

import com.example.indiebeauty.domain.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	// categoryId로 상품 검색 
	List<Product> findByCategory_CategoryId(int categoryId);
	
	// 키워드로 상품 검색 (상품 이름 기준)
	List<Product> findByNameLike(String keyword);
	
	// 키워드로 상품 검색 (카테고리 이름 기준)
	List<Product> findByCategory_NameLike(String keyword);
	
	// 최근 등록된 상품 검색 (모든 상품 기준)
	List<Product> findByOrderByDateDesc();
	
	// 최근 등록된 상품 검색 (카테고리 기준)
	List<Product> findByCategory_CategoryIdOrderByDateDesc(int categoryId);
}