package com.example.indiebeauty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.indiebeauty.domain.Cart;

@Controller
@SessionAttributes("sessionCart")
public class RemoveProductFromCartController { 

	@RequestMapping("/shop/removeProductFromCart.do")
	public ModelAndView handleRequest(
			@RequestParam("workingProductId") int workingProductId,
			@ModelAttribute("sessionCart") Cart cart
		) throws Exception {
		cart.removeProductById(workingProductId);
		return new ModelAndView("Cart", "cart", cart);
	}
}
