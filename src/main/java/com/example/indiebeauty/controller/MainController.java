package com.example.indiebeauty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.service.CategoryService;
import com.example.indiebeauty.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/")
	public ModelAndView mainPage(HttpSession session) {
		List<Category> categoryList = categoryService.getCategoryList();
		session.setAttribute("categoryList", categoryList);
		
		ModelAndView mav = new ModelAndView("index");
		
		// 최신 등록 상품 조회 (6개)
		Map<String, Object> mapResult = productService.getAllProductWithTitleImage(1, 6);
		
		@SuppressWarnings("unchecked")
		List<Product> allProducts = (List<Product>) mapResult.get("products");
		mav.addObject("allProducts", allProducts);
		
		return mav;
	}
}
