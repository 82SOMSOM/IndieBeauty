package com.example.indiebeauty.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.exception.FileUploadException;
import com.example.indiebeauty.service.CategoryService;
import com.example.indiebeauty.service.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
			@Valid @ModelAttribute("uploadProduct") UploadProduct uploadProduct,
			BindingResult bindingResult, 
			RedirectAttributes ra, 
			SessionStatus status,
			HttpSession session) {
		if (bindingResult.hasErrors()) {
			ra.addFlashAttribute("org.springframework.validation.BindingResult.uploadProduct", bindingResult);
			ra.addFlashAttribute("uploadProduct", uploadProduct);
			
			return "redirect:/upload-product";
		}
		
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
	
	@InitBinder("uploadProduct")
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Integer.class, "price", new CustomNumberEditor(Integer.class, null, true) {
	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	        	try {
                    // 쉼표 제거 후 숫자로 변환
                    String sanitizedText = text.replaceAll(",", "").trim();
                    if (sanitizedText.isEmpty()) {
                        setValue(null);
                    } else {
                        setValue(Integer.parseInt(sanitizedText));
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid number format. Please enter a valid integer.");
                }
	        }
	    });
	    
	    binder.registerCustomEditor(Integer.class, "stock", new CustomNumberEditor(Integer.class, null, true) {
	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	        	try {
                    // 쉼표 제거 후 숫자로 변환
                    String sanitizedText = text.replaceAll(",", "").trim();
                    if (sanitizedText.isEmpty()) {
                        setValue(null);
                    } else {
                        setValue(Integer.parseInt(sanitizedText));
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid number format. Please enter a valid integer.");
                }
	        }
	    });
	}
}
