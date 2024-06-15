package com.example.indiebeauty.service;

import java.util.List;

import com.example.indiebeauty.domain.UserInfo;

public interface IndiebeautyFacade {
	UserInfo getUserInfo(String userid);
	
	UserInfo getUserInfo(String userid, String passwd);
	
	void insertUserInfo(UserInfo userinfo);
	void updateUserInfo(UserInfo userinfo);
	
//	List<String> getUserIdList();
}
