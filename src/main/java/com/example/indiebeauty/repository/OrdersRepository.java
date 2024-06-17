package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	// getOrder는 service에서 getReferenceById 사용, insertOrder, deleteOrder service에 자체
	// 코드 작성 후 사용

	//사용자 아이디로 주문 찾기
	List<Orders> findByUserId(String userid);
}
