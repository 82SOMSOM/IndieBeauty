package com.example.indiebeauty.repository;

import com.example.indiebeauty.domain.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	List<Review> findByProduct_ProductId(int productId); // 0618 추가
	
	@Query("SELECT AVG(r.star) FROM Review r WHERE r.product.productId = :productId")
    Double findAverageRatingByProductId(int productId); // 0618 추가
}
