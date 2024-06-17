package com.example.indiebeauty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception{
		UserSession userSession =
				(UserSession) WebUtils.getSessionAttribute(request, "userSession");
		if(userSession == null) {
			logger.info("userSesson == null");
			String url = request.getRequestURL().toString();
			String query = request.getQueryString();
			ModelAndView modelAndView = new ModelAndView("login");
			if(query != null) {
				logger.info("query != null");
				modelAndView.addObject("loginForwardAction", url+"?"+query);
			}
			else {
				modelAndView.addObject("loginForwardAction", url);
			}
			throw new ModelAndViewDefiningException(modelAndView);
		}
		else {
			return true;
		}		
	}
}
