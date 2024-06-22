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
	public boolean supports(Class<?> clazz) {
		return UserInfo.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		UserForm userForm = (UserForm)obj;
		UserInfo userinfo = userForm.getUserInfo();
		
		if (userForm.isNewUserInfo()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "UserInfo.userid", "USER_ID_REQUIRED","userid is required");
		}
		
	}

}
