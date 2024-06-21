package com.example.indiebeauty.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.domain.SellerInfo;

@SuppressWarnings("serial")
public class SellerForm implements Serializable{
	private SellerInfo sellerInfo;
	private boolean newSellerInfo;
	private String repeatedPasswd;
	private List<MultipartFile> BusinessList;
	
	public List<MultipartFile> getBusinessList() {
		return BusinessList;
	}

	public void setBusinessList(List<MultipartFile> businessList) {
		BusinessList = businessList;
	}

	public SellerForm(SellerInfo sellerInfo) {
		this.sellerInfo = sellerInfo;
		this.newSellerInfo = false;
	}
	
	public SellerForm() {
		this.sellerInfo = new SellerInfo();
		this.newSellerInfo = true;
	}
	
	public SellerInfo getSellerInfo() {
		return sellerInfo;
	}
	
    public void setUserInfo(SellerInfo sellerInfo) {
        this.sellerInfo = sellerInfo;
    }
    
	public boolean isNewSellerInfo() {
		return newSellerInfo;
	}
	
	public void setRepeatedPasswd(String repeatedPasswd) {
		this.repeatedPasswd = repeatedPasswd;
	}
	
	public String getRepeatedPasswd() {
		return repeatedPasswd;
	}
}
