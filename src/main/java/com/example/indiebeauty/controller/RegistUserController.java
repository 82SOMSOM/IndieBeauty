package com.example.indiebeauty.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
	
//	회원가입
	@GetMapping("/user/checkUserId")
	public ResponseEntity<?> checkUserId(@RequestParam String userid) {
	    boolean isAvailable = !indiebeauty.existsUserId(userid);
	    return ResponseEntity.ok().body(Map.of("available", isAvailable));
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
  
//	회원정보 수정
	@RequestMapping(value = "/showUserInfo", method = RequestMethod.GET)
    public String showEditForm(HttpServletRequest request, Model model) {
        UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
        logger.info("마이페이지 접근 시도 - 현재 userSession 상태: " + userSession);
        if (userSession != null && userSession.getUserInfo() != null) {
            UserForm userForm = new UserForm(indiebeauty.getUserInfo(userSession.getUserInfo().getUserid()));
            logger.info("세션 있음 - 사용자 ID: " + userSession.getUserInfo().getUserid());
            model.addAttribute("userForm", userForm);
            logger.info("마이페이지로 이동");
            return "showUserInfo";  // 정보 수정 페이지로 이동
        }else {
	        logger.info("session 없음");
	        return "redirect:/login";  // 세션이 없으면 회원 가입 페이지로 리다이렉트
        }
    }
	@RequestMapping(value = "/editUserInfo", method = RequestMethod.GET)
    public String editUserInfo() {
		logger.info("회원정보 수정 페이지 이동");
        return "editUserInfo";  // 회원가입 페이지로 이동
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
            return "redirect:/shop?pageNum=1";
        } catch (DataIntegrityViolationException ex) {
        	logger.error("회원정보 수정 에러");
            result.rejectValue("UserInfo.userid", "ERROR", "An error occurred during the update.");
            return "editUserInfo";
        }
    }
    
//  회원 탈퇴
    @GetMapping("/user/deleteUser")
    public String deleteUser(HttpSession session, RedirectAttributes redirectAttrs) {
        try {
            UserSession userSession = (UserSession) session.getAttribute("userSession");
            if (userSession != null && userSession.getUserInfo() != null) {
                // 사용자와 관련된 모든 데이터를 먼저 삭제
                indiebeauty.deleteAllUserRelatedData(userSession.getUserInfo().getUserid());
                // 사용자 정보 삭제
                indiebeauty.deleteUserInfo(userSession.getUserInfo().getUserid());
                session.invalidate();
                return "redirect:/login";
            } else {
                return "redirect:/login";
            }
        } catch (DataIntegrityViolationException ex) {
            redirectAttrs.addFlashAttribute("error", "Failed to delete account. Please contact support.");
            return "redirect:/showUserInfo"; // 회원 정보 페이지로 리다이렉트
        }
    }
    
}
