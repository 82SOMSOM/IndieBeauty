package com.example.indiebeauty.controller;

import java.io.Serializable;

import com.example.indiebeauty.domain.UserInfo;

@SuppressWarnings("serial")
public class UserSession implements Serializable{
	
	private UserInfo userinfo;
	
	public UserSession(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
	
	public UserInfo getUserInfo() {
		return userinfo;
	}

}
