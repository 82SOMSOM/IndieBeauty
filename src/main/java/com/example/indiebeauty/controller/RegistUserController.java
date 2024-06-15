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
//@RequestMapping({"/user/registerUser", "/user/editUser"})
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
		logger.error("formbacking method 이동");
		UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
		if (userSession != null) {	// edit an existing user
			return new UserForm(
				indiebeauty.getUserInfo(userSession.getUserInfo().getUserid()));
		}
		else {	// create a new user
			return new UserForm();
		}
	}
	
//	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	@GetMapping("/signin")
    public String showRegistrationForm() {
		logger.error("회원가입페이지 이동");
        return "signin";  // 회원가입 페이지로 이동
    }
//  @PostMapping("/user/registerUser")
  @RequestMapping(value = "/user/registerUser", method = RequestMethod.POST)
  public String registerUser(@ModelAttribute("userForm") UserForm userForm, BindingResult result, HttpSession session) {
	  logger.error("registerUser method 이동");
	  validator.validate(userForm, result);
	  logger.error("validator 이동");
      if (result.hasErrors()) {
          return "signin";
      }
      try {
          indiebeauty.insertUserInfo(userForm.getUserInfo());
          logger.info("회원가입 완료");
          return "redirect:/login.html";
      } catch (DataIntegrityViolationException ex) {
          result.rejectValue("UserInfo.userid", "USER_ID_ALREADY_EXISTS", "User ID already exists: choose a different ID.");
          return "signin";
      }
  }
  
	@RequestMapping(value = "/editUserInfo", method = RequestMethod.GET)
//	@GetMapping("/editUserInfo")
    public String showEditForm(HttpServletRequest request, Model model) {
        UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
        if (userSession != null) {
            UserForm userForm = new UserForm(indiebeauty.getUserInfo(userSession.getUserInfo().getUserid()));
            model.addAttribute("userForm", userForm);
            return "editUserInfo";  // 정보 수정 페이지로 이동
        }
        return "redirect:/shop";  // 세션이 없으면 회원 가입 페이지로 리다이렉트
    }
//    @PostMapping("/user/editUser")
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
            result.rejectValue("userInfo.userid", "ERROR", "An error occurred during the update.");
            return "editUserInfo";
        }
    }
	
//	@RequestMapping(value = {"/user/registerUser", "/user/editUser"}, method = RequestMethod.POST)
//    public String onSubmit(HttpServletRequest request, HttpSession session,
//                           @ModelAttribute("userForm") UserForm userForm,
//                           BindingResult result) {
//        validator.validate(userForm, result);
//
//        if(result.hasErrors()) return "editUserInfo";
//        try {
//            if (userForm.isNewUserInfo()) {
//                indiebeauty.insertUserInfo(userForm.getUserInfo());
//                logger.error("new UserInfo");
//            } else {
//                indiebeauty.updateUserInfo(userForm.getUserInfo());
//                logger.error("edit UserInfo");
//            }
//        } catch (DataIntegrityViolationException ex) {
//        	result.rejectValue("userinfo.userid", "USER_ID_ALREADY_EXISTS",
//        			"User ID alreay exists: choose a different ID.");
//        	return "editUserInfo";
//        }
//        UserSession newUserSession = new UserSession(
//        		indiebeauty.getUserInfo(userForm.getUserInfo().getUserid()));
//        session.setAttribute("userSession", newUserSession);
//        logger.error("회원가입 완료");
////        return "redirect:/shop";  // 성공적으로 처리 후 쇼핑 페이지로 리다이렉트
//        return "redirect/login";
//    }
	 	
}
