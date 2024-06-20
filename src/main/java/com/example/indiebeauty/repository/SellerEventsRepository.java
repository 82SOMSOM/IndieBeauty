package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.SellerEvents;

public interface SellerEventsRepository extends JpaRepository<SellerEvents, Integer> {

	List<SellerEvents> findByOrderByDateDesc();
}