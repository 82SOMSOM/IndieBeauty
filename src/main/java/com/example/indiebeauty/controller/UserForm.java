package com.example.indiebeauty.controller;

import java.io.Serializable;

import com.example.indiebeauty.domain.UserInfo;

@SuppressWarnings("serial")
public class UserForm implements Serializable{
	
	private UserInfo userInfo;
	
	private boolean newUserInfo;
	
	private String repeatedPasswd;
	
	public UserForm(UserInfo userInfo) {
		this.userInfo = userInfo;
		this.newUserInfo = false;
	}
	
	public UserForm() {
		this.userInfo = new UserInfo();
		this.newUserInfo = true;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
	public boolean isNewUserInfo() {
		return newUserInfo;
	}
	
	public void setRepeatedPasswd(String repeatedPasswd) {
		this.repeatedPasswd = repeatedPasswd;
	}
	
	public String getRepeatedPasswd() {
		return repeatedPasswd;
	}
	
}
