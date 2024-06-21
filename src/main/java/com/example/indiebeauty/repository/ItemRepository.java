package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Orders;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	
	List<Item> findByOrderId(int orderId);
}
