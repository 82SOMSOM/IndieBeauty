package com.example.indiebeauty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.indiebeauty.controller.SellerForm;
import com.example.indiebeauty.domain.SellerInfo;
import com.example.indiebeauty.domain.UserInfo;

@Component
public class SellerFormValidator implements Validator{
	public boolean supports(Class<?> clazz) {
		return UserInfo.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		SellerForm sellerForm = (SellerForm)obj;
		SellerInfo sellerinfo = sellerForm.getSellerInfo();

		if (sellerForm.isNewSellerInfo()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "SellerInfo.sellerid", "Seller_ID_REQUIRED","sellerid is required");

		}
	}

}


