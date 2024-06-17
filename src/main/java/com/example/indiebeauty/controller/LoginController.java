package com.example.indiebeauty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.service.IndiebeautyFacade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("userSession")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
//	사용자 로그인
	@RequestMapping(value="/login", method = RequestMethod.GET )
	public String loginForm(){
		logger.info("login 페이지 이동");
		return "login";
	}
	
//	판매자 로그인
	@RequestMapping(value = "/sellerLogin", method = RequestMethod.GET)
    public String showRegistrationForm() {
		logger.info("판매자 로그인페이지 이동");
        return "sellerLogin";  // 회원가입 페이지로 이동
    }
	
	@RequestMapping(value="/shop", method = RequestMethod.GET )
	public String shopPage(){
		logger.info("shop 페이지 이동");
		return "shop";
	}
	
	private IndiebeautyFacade indiebeauty;
	@Autowired
	public void setIndiebeauty(IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}
	
	@RequestMapping(value="/user/login", method = RequestMethod.POST)
	public ModelAndView loginUser(HttpServletRequest request,
			@RequestParam("userid") String userid,
			@RequestParam("passwd") String passwd,
			@RequestParam(value="forwardAction", required=false) String forwardAction,
			Model model) throws Exception {
		UserInfo userinfo = indiebeauty.getUserInfo(userid, passwd);
		logger.info("UserInfo 불러옴");
//		if (userinfo == null) {
//			return new ModelAndView("Error", "message",
//					"Invalid userId or password.  login failed.");
//		}
//		else {
			UserSession userSession = new UserSession(userinfo);
			model.addAttribute("userSession", userSession);
			logger.info("session 저장 성공" + userSession.getUserInfo().getUserid());
			if (forwardAction != null) {
				return new ModelAndView("redirect:" + forwardAction);
			}
			else {
				logger.info("login 성공");
				return new ModelAndView("shop");
			}
//		}
	}

//  로그아웃
  @RequestMapping("/logout")
	public String handleRequest(HttpSession session, SessionStatus sessionStatus) throws Exception{
        logger.info("세션 제거 전"); // 로그아웃 전 세션 상태 로깅
        sessionStatus.setComplete(); // 세션 어트리뷰트 정리
        session.invalidate();
        logger.info("logout 성공 - 세션 제거 후");
        return "redirect:/shop";
	}
	
}
