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

import com.example.indiebeauty.domain.SellerInfo;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.service.IndiebeautyFacade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes({"userSession", "sellerSession"})
public class LoginController {
	private IndiebeautyFacade indiebeauty;
	@Autowired
	public void setIndiebeauty(IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}
	
//	사용자 로그인
	@RequestMapping(value="/login", method = RequestMethod.GET )
	public String loginForm(){
		return "login";
	}	
	@RequestMapping(value="/user/login", method = RequestMethod.POST)
	public ModelAndView loginUser(HttpServletRequest request,
			@RequestParam("userid") String userid,
			@RequestParam("passwd") String passwd,
			@RequestParam(value="forwardAction", required=false) String forwardAction,
			Model model) throws Exception {
		UserInfo userinfo = indiebeauty.getUserInfo(userid, passwd);
		if (userinfo == null) {// 로그인 실패 시 에러 메시지를 포함하여 login 페이지로
			return new ModelAndView("/login", "message", 
					"Invalid userid or password.  Login failed.");
		}
		else {
			UserSession userSession = new UserSession(userinfo);
			model.addAttribute("userSession", userSession);
			if (forwardAction != null) {
				return new ModelAndView("redirect:" + forwardAction);
			}
			else {
				return new ModelAndView("redirect:/shop?pageNum=1");
			}
		}
	}

//	판매자 로그인
	@RequestMapping(value = "/sellerLogin", method = RequestMethod.GET)
    public String showRegistrationForm() {
        return "sellerLogin";  // 회원가입 페이지로 이동
    }
	@RequestMapping(value="/seller/login", method = RequestMethod.POST)
	public ModelAndView loginSeller(HttpServletRequest request,
			@RequestParam("sellerid") String sellerid,
			@RequestParam("passwd") String passwd,
			@RequestParam(value="forwardAction", required=false) String forwardAction,
			Model model) throws Exception {
		SellerInfo sellerinfo = indiebeauty.getSellerInfo(sellerid, passwd);
		if (sellerinfo == null) {
			// 로그인 실패 시 에러 메시지를 포함하여 login 페이지로 
			return new ModelAndView("/sellerLogin", "message", 
					"Invalid sellerid or password.  Login failed.");
		}
		else {
			SellerSession sellerSession = new SellerSession(sellerinfo);
			model.addAttribute("sellerSession", sellerSession);
			if (forwardAction != null) {
				return new ModelAndView("redirect:" + forwardAction);
			}
			else {
				return new ModelAndView("redirect:/shop?pageNum=1");
			}
		}
	}
	
	
//  로그아웃
	@RequestMapping("/logout")
	public String handleRequest(HttpSession session, SessionStatus sessionStatus) throws Exception{
        sessionStatus.setComplete(); // 세션 어트리뷰트 정리
        session.invalidate();
        return "redirect:/shop?pageNum=1";
	}
	
}
