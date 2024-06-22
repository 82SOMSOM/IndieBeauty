package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	
	Page<Orders> findByUserId(String userId, Pageable pageable);

	void deleteByUserId(String userid);
}
