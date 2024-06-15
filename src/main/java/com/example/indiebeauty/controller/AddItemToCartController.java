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
import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.service.IndiebeautyFacade;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("sessionCart")
public class AddItemToCartController { 

	private IndiebeautyFacade indieBeauty;

	@Autowired
	public void setIndieBeauty(IndiebeautyFacade indieBeauty) {
		this.indieBeauty = indieBeauty;
	}

	@ModelAttribute("sessionCart")
	public Cart createCart() {
		return new Cart();
	}
	
	@RequestMapping("/shop/addItemToCart.do")
	public ModelAndView handleRequest(
			@RequestParam("workingItemId") String workingItemId,
			@ModelAttribute("sessionCart") Cart cart, HttpServletRequest request 
			) throws Exception {
		if (cart.containsItemId(workingItemId)) {
			//////////////////////////////////////////////
			CartItem ci = cart.incrementQuantityByItemId(workingItemId);
			indieBeauty.updateCartItem(ci);
		}
		else {
			// isInStock is a "real-time" property that must be updated
			// every time an item is added to the cart, even if other
			// item details are cached.
			boolean isInStock = this.indieBeauty.isItemInStock(workingItemId);
			Item item = this.indieBeauty.getItem(workingItemId);
			
			//////////////////////////////////////////////
			CartItem ci = cart.addItem(item, isInStock);
			
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			if (userSession != null) {	// logged-in 			
				ci.setUserId(userSession.getAccount().getUsername());
				if (ci.getQuantity() == 1)
					indieBeauty.insertCartItem(ci);
				else 
					indieBeauty.updateCartItem(ci);
			//////////////////////////////////////////////
			}
		}
		return new ModelAndView("Cart", "cart", cart);
	}
}