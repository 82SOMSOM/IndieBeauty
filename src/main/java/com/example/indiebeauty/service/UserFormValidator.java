package com.example.indiebeauty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.indiebeauty.controller.UserForm;
import com.example.indiebeauty.domain.UserInfo;

@Component
public class UserFormValidator implements Validator{
	private static final Logger logger = LoggerFactory.getLogger(UserFormValidator.class);
	public boolean supports(Class<?> clazz) {
		return UserInfo.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		UserForm userForm = (UserForm)obj;
		UserInfo userinfo = userForm.getUserInfo();
		
		if (userForm.isNewUserInfo()) {
			logger.info("isNewUserInfo 이동");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "UserInfo.userid", "USER_ID_REQUIRED","userid is required");
			logger.info("userid 확인");
//			if(userinfo.getPasswd() == null || userinfo.getPasswd().length() < 1 ||
//					!userinfo.getPasswd().equals(userForm.getRepeatedPasswd())) {
//				errors.reject("PASSWORD_MISMATCH", 
//						"Passwords did not match or were not provided. Matching passwords are required.");
//			}
//			logger.info("password 확인");
		}
//		else if (userinfo.getPasswd() != null && userinfo.getPasswd().length() > 0) {
//			if (!userinfo.getPasswd().equals(userForm.getRepeatedPasswd())) {
//				errors.reject("PASSWORD_MISMATCH", "Passwords did not match. Matching passwords are required.");
//			}
//		}
		
	}

}
