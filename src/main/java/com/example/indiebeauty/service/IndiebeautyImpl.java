package com.example.indiebeauty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.repository.UserRepository;

@Service
@Transactional
public class IndiebeautyImpl implements IndiebeautyFacade {
	@Autowired
	private UserRepository userRepository;
	
	public UserInfo getUserInfo(String userid) {
		return userRepository.getReferenceById(userid);
	}
	public UserInfo getUserInfo(String userId, String passwd) {
		return userRepository.findByUseridAndPasswd(userId, passwd);
	}
	
	public void insertUserInfo(UserInfo userinfo) {
		userRepository.save(userinfo);
	}
	public void updateUserInfo(UserInfo userinfo) {
		userRepository.save(userinfo);
	}

	@Override
	public boolean isProductInStock(int workingProductId) {
//		return productRepository.existsByItemIdAndQuantityGreaterThan(itemId, 0);
		return false;
	}
	@Override
	public Product getProduct(int workingProductId) {
//		return productRepository.getReferenceById(itemId);
		return null;
	}
	
//	public List<String> getUserIdList(){
//		return userRepository.findUserIds();
//	}
}
