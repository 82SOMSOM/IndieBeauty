package com.example.indiebeauty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);
	
//	@RequestMapping("/logout")
//	public String handleRequest(HttpSession session) throws Exception{
//		session.removeAttribute("userSession");
//		session.invalidate();
//		logger.info("logout 성공");
//		return "shop";
//	}
}
