package com.example.indiebeauty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;
import com.example.indiebeauty.service.IndiebeautyFacade;

@Controller
@SessionAttributes("sessionCart")
public class RemoveItemFromCartController { 
	private IndiebeautyFacade indieBeauty;

	@Autowired
	public void setPetStore(IndiebeautyFacade indieBeauty) {
		this.indieBeauty = indieBeauty;
	}
	
	@RequestMapping("/shop/removeItemFromCart.do")
	public ModelAndView handleRequest(
			@RequestParam("workingItemId") int workingItemId,
			@ModelAttribute("sessionCart") Cart cart
		) throws Exception {
		
		//////////////////////////////////////////////
		CartItem ci = cart.removeItemById(workingItemId);
		if (ci != null) indieBeauty.deleteCartItem(ci);
		//////////////////////////////////////////////
		
		return new ModelAndView("Cart", "cart", cart);
	}
}
