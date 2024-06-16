package com.example.indiebeauty.controller;

import java.io.Serializable;
import org.springframework.beans.support.PagedListHolder;

import com.example.indiebeauty.domain.CartItem;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.UserInfo;

@SuppressWarnings("serial")
public class UserSession implements Serializable{
	
	private UserInfo userinfo;
	private PagedListHolder<Product> myList;
	
	public UserSession(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
	
	public UserInfo getUserInfo() {
		return userinfo;
	}

	public void setMyList(PagedListHolder<Product> myList) {
		this.myList = myList;
	}

	public PagedListHolder<Product> getMyList() {
		return myList;
	}
}
