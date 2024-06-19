package com.example.indiebeauty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.exception.NoSuchProductException;
import com.example.indiebeauty.service.CategoryService;
import com.example.indiebeauty.service.ProductService;
import com.example.indiebeauty.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ReviewService reviewService; // 0618 추가
	
	public ModelAndView viewProductByCategory(String category) {
		return null;
	}
	
	@GetMapping("/shop/product-detail/{productId}")		// 상품 상세 조회
	public ModelAndView viewProductDetail(@PathVariable("productId") int productId, RedirectAttributes ra) {
		try {
			Product product = productService.getProductById(productId);
			List<Review> reviews = reviewService.getReviewsByProductId(productId); // 0618 추가
			double averageRating = reviewService.getAverageRating(productId); // 0618 추가
			
			System.out.println(product.toString());
			
			ModelAndView mav = new ModelAndView("productDetails");
			mav.addObject("product", product);
			mav.addObject("reviews", reviews); // 0618 추가
			mav.addObject("averageRating", averageRating); // 0618 추가
			
			return mav;
		} catch (NoSuchProductException e) {
			String msg = e.getMessage();
			
			ra.addAttribute("msg", msg);
			ra.addAttribute("url", "/shop");
			
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/shop/productDetail/error");
			
			return mav;
		}
		
	}
	
	@GetMapping("/shop/category/{categoryId}")	// 카테고리별 상품 조회
	@SuppressWarnings(value = "unchecked")
	public ModelAndView viewProductByCategory(@PathVariable("categoryId") int categoryId, @RequestParam("pageNum") int pageNum, HttpSession session) {
		List<Category> categoryList = categoryService.getCategoryList();
		session.setAttribute("categoryList", categoryList);
		
		Map<String, Object> resultMap = productService.getProductByCategoryIdWithTitleImage(categoryId, pageNum, 9);
		List<Product> products = (List<Product>) resultMap.get("products");
		int totalPages = (int) resultMap.get("totalPages");
		
		ModelAndView mav = new ModelAndView("shop");
		mav.addObject("products", products);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", pageNum);
		mav.addObject("currentUrl", "/shop/category/" + categoryId);
		
		return mav;
	}
	
	@GetMapping("/shop")	// 모든 상품 조회
	@SuppressWarnings(value = "unchecked")
	public ModelAndView viewAllProduct(HttpSession session, @RequestParam("pageNum") int pageNum) {
		List<Category> categoryList = categoryService.getCategoryList();
		session.setAttribute("categoryList", categoryList);
		
		Map<String, Object> resultMap = productService.getAllProductWithTitleImage(pageNum, 9);
		List<Product> products = (List<Product>) resultMap.get("products");
		int totalPages = (int) resultMap.get("totalPages");
		
		ModelAndView mav = new ModelAndView("shop");
		mav.addObject("products", products);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", pageNum);
		mav.addObject("currentUrl", "/shop");
		
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/shop/search")	// 상품 검색
	public ModelAndView searchProduct(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum, HttpSession session) {
		List<Category> categoryList = categoryService.getCategoryList();
		session.setAttribute("categoryList", categoryList);
		
		Map<String, Object> resultMap = productService.getProductListByKeywordWithTitleImage(keyword, pageNum);
		List<Product> products = (List<Product>) resultMap.get("products");
		int totalPages = (int) resultMap.get("totalPages");
		
		ModelAndView mav = new ModelAndView("shop");
		mav.addObject("products", products);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", pageNum);
		mav.addObject("currentUrl", "/shop/search?keyword=" + keyword);
		
		return mav;
	}
	
	@GetMapping("/shop/productDetail/error")
	public String alert(@ModelAttribute("msg") String msg, @ModelAttribute("url") String url) {
		return "alert";
	}
}
