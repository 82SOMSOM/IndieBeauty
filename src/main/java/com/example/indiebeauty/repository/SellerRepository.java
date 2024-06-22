package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.indiebeauty.domain.SellerInfo;


public interface SellerRepository extends JpaRepository<SellerInfo, String>{
	SellerInfo findBySelleridAndPasswd(String sellerid, String password);
}
