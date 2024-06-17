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
import com.example.indiebeauty.service.ReviewService;
import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.exception.FileUploadException;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("uploadReview")
public class RegistReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@ModelAttribute("uploadReview")
	public UploadReview formData() {
		return new UploadReview();
	}
	
	@GetMapping("/upload-review")
	public String initUploadReview(HttpSession session) {
		return "uploadReview";
	}
	
	@PostMapping("/upload-review")
	public String registerReview(@ModelAttribute("review") UploadReview uploadReview,
			RedirectAttributes ra, SessionStatus status) {
		try {
			reviewService.registerReview(uploadReview);
			status.setComplete();
			
			// @FIXME redirect 주소 변경
			return "redirect:/upload-review";
		} catch (FileUploadException e) {
			String msg = e.getMessage();
			
			ra.addAttribute("msg", msg);
			ra.addAttribute("url", "/upload-review");
			
			return "redirect:/upload-review/error";
		}
	}
	
	@GetMapping("/upload-review/error")
	public String alert(@ModelAttribute("msg") String msg, @ModelAttribute("url") String url) {
		return "alert";
	}
}
