package com.example.indiebeauty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.example.indiebeauty.service.IndiebeautyFacade;
import com.example.indiebeauty.service.UserFormValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class RegistUserController {
	private static final Logger logger = LoggerFactory.getLogger(RegistUserController.class);
	
	@Autowired
	private IndiebeautyFacade indiebeauty;
	public void setIndiebeautyFacade (IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}
	
	@Autowired
	private UserFormValidator validator;
	public void setValidator(UserFormValidator validator) {
		this.validator = validator;
	}
	
	@ModelAttribute("userForm")
	public UserForm formBackingObject(HttpServletRequest request) throws Exception{
		logger.info("formbacking method 이동");
		UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
		if (userSession != null) {	// edit an existing user
			return new UserForm(
				indiebeauty.getUserInfo(userSession.getUserInfo().getUserid()));
		}
		else {	// create a new user
			return new UserForm();
		}
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String showRegistrationForm() {
		logger.info("회원가입페이지 이동");
        return "signin";  // 회원가입 페이지로 이동
    }

	@RequestMapping(value = "/user/registerUser", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("userForm") UserForm userForm, BindingResult result, HttpSession session) {
		logger.info("registerUser method 이동");
		validator.validate(userForm, result);
		logger.info("validator 이동");
		if (result.hasErrors()) {
			logger.error("error 발생");
			return "signin";
		}
		try {
			indiebeauty.insertUserInfo(userForm.getUserInfo());
			logger.info("회원가입 완료");
			return "redirect:/login";
		} catch (DataIntegrityViolationException ex) {
			result.rejectValue("UserInfo.userid", "USER_ID_ALREADY_EXISTS", "User ID already exists: choose a different ID.");
			return "signin";
		}
	}
  
	@RequestMapping(value = "/editUserInfo", method = RequestMethod.GET)
    public String showEditForm(HttpServletRequest request, Model model) {
        UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
        if (userSession != null) {
            UserForm userForm = new UserForm(indiebeauty.getUserInfo(userSession.getUserInfo().getUserid()));
            model.addAttribute("userForm", userForm);
            return "editUserInfo";  // 정보 수정 페이지로 이동
        }
        return "redirect:/singin";  // 세션이 없으면 회원 가입 페이지로 리다이렉트
    }

    @RequestMapping(value = "/user/editUser", method = RequestMethod.POST)
    public String editUser(@ModelAttribute("userForm") UserForm userForm, BindingResult result, HttpSession session) {
        validator.validate(userForm, result);
        if (result.hasErrors()) {
            return "editUserInfo";
        }
        try {
            indiebeauty.updateUserInfo(userForm.getUserInfo());
            logger.info("회원정보 수정 완료");
            return "redirect:/shop.html";
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue("UserInfo.userid", "ERROR", "An error occurred during the update.");
            return "editUserInfo";
        }
    }
    
	@RequestMapping("/logout")
	public String logoutUser(HttpSession session) throws Exception{
		session.removeAttribute("userSession");
		session.invalidate();
		logger.info("logout 성공");
		return "redirect:shop";
	}
	 	
}
