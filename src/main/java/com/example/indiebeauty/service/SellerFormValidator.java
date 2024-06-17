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
	private static final Logger logger = LoggerFactory.getLogger(SellerFormValidator.class);
	public boolean supports(Class<?> clazz) {
		return UserInfo.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		SellerForm sellerForm = (SellerForm)obj;
		SellerInfo sellerinfo = sellerForm.getSellerInfo();

		if (sellerForm.isNewSellerInfo()) {
			logger.info("isNewSellerrInfo 이동");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "SellerInfo.sellerid", "Seller_ID_REQUIRED","sellerid is required");
			logger.info("sellerid 확인");
//			if(userinfo.getPasswd() == null || userinfo.getPasswd().length() < 1 ||
//					!userinfo.getPasswd().equals(userForm.getRepeatedPasswd())) {
//				errors.reject("PASSWORD_MISMATCH", 
//						"Passwords did not match or were not provided. Matching passwords are required.");
//			}
//			logger.info("password 확인");
		}
		//else if (userinfo.getPasswd() != null && userinfo.getPasswd().length() > 0) {
//			if (!userinfo.getPasswd().equals(userForm.getRepeatedPasswd())) {
//				errors.reject("PASSWORD_MISMATCH", "Passwords did not match. Matching passwords are required.");
//			}
		//}
	}

}


