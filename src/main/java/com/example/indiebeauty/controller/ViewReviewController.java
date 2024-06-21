package com.example.indiebeauty.controller;

import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.service.IndiebeautyFacade;
import com.example.indiebeauty.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/reviews")
public class ViewReviewController {
	
	private IndiebeautyFacade indiebeauty;

	@Autowired
	public void setIndiebeauty(IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}
	
	@RequestMapping("/review/viewReview.do")
	public String handleRequest(
			@RequestParam("reviewId") int reviewId,
			ModelMap model) throws Exception {
		Review review = this.indiebeauty.getReview(reviewId);
		model.put("review", review);
		return "Review";
	}
}
