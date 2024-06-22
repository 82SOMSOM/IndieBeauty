package com.example.indiebeauty.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.SellerInfo;


public interface SellerRepository extends JpaRepository<SellerInfo, String>{
	SellerInfo findBySelleridAndPasswd(String sellerid, String password);
}
