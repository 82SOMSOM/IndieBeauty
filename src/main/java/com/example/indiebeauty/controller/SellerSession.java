package com.example.indiebeauty.controller;

import java.io.Serializable;

import com.example.indiebeauty.domain.SellerInfo;
@SuppressWarnings("serial")
public class SellerSession implements Serializable {
	private SellerInfo sellerinfo;
	
	public SellerSession(SellerInfo sellerinfo) {
		this.sellerinfo = sellerinfo;
	}
	
	public SellerInfo getSellerInfo() {
		return sellerinfo;
	}
}
