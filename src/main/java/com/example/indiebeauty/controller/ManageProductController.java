package com.example.indiebeauty.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.indiebeauty.service.CategoryService;
import com.example.indiebeauty.service.ProductService;
import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.exception.FileUploadException;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("uploadProduct")
public class ManageProductController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	@ModelAttribute("uploadProduct")
	public UploadProduct formData() {
		return new UploadProduct();
	}
	
	@GetMapping("/upload-product")
	public String initUploadProduct(HttpSession session, RedirectAttributes ra) {
		SellerSession sellerSession = (SellerSession) session.getAttribute("sellerSession");
		if (sellerSession == null) {
			ra.addAttribute("msg", "판매자만 접근 가능합니다");
			ra.addAttribute("url", "/");
			return "redirect:/upload-product/error";
		}
		
		List<Category> categoryList = categoryService.getCategoryList();
		session.setAttribute("categoryList", categoryList);
		
		return "uploadProduct";
	}
	
	@PostMapping("/upload-product")
	public String registerProduct(
			@ModelAttribute("uploadProduct") UploadProduct uploadProduct,
			RedirectAttributes ra, 
			SessionStatus status,
			HttpSession session) {
		SellerSession sellerSession = (SellerSession) session.getAttribute("sellerSession");
		
		try {
			int newProductId = productService.registerProduct(uploadProduct, sellerSession.getSellerInfo());
			status.setComplete();
			
			return ("redirect:/shop/product-detail/" + newProductId);
		} catch (FileUploadException e) {
			String msg = e.getMessage();
			
			ra.addAttribute("msg", msg);
			ra.addAttribute("url", "/upload-product");
			
			return "redirect:/upload-product/error";
		}
	}
	
	@GetMapping("/upload-product/error")
	public String alert(@ModelAttribute("msg") String msg, @ModelAttribute("url") String url) {
		return "alert";
	}
}
