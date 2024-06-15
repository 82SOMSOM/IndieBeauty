package com.example.indiebeauty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
//	public List<String> getUserIdList(){
//		return userRepository.findUserIds();
//	}
}
