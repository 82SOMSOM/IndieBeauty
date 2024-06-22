package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.SellerEvents;

public interface SellerEventsRepository extends JpaRepository<SellerEvents, Integer> {

	List<SellerEvents> findByOrderByDateDesc();

	Page<SellerEvents> findAll(Pageable pageable);
	
//	void deleteBySellerId(String sellerid);
}