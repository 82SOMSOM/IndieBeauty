package com.example.indiebeauty.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.indiebeauty.controller.UserForm;
import com.example.indiebeauty.domain.UserInfo;

@Component
public class UserFormValidator implements Validator{
	public boolean supports(Class<?> clazz) {
		return UserInfo.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		UserForm userForm = (UserForm)obj;
		UserInfo userinfo = userForm.getUserInfo();
		
		if (userForm.isNewUserInfo()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userinfo.userId", "USER_ID_REQUIRED","userId is required");
			if(userinfo.getPasswd() == null || userinfo.getPasswd().length() < 1 ||
					!userinfo.getPasswd().equals(userForm.getRepeatedPasswd())) {
				errors.reject("PASSWORD_MISMATCH", 
						"Passwords did not match or were not provided. Matching passwords are required.");
			}
		}
		else if (userinfo.getPasswd() != null && userinfo.getPasswd().length() > 0) {
			if (!userinfo.getPasswd().equals(userForm.getRepeatedPasswd())) {
				errors.reject("PASSWORD_MISMATCH", "Passwords did not match. Matching passwords are required.");
			}
		}
	}

}
