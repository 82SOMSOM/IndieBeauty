package com.example.indiebeauty.controller;

import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;
import com.example.indiebeauty.service.IndiebeautyFacade;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("sessionCart")
public class UpdateCartQuantitiesController { 
	private IndiebeautyFacade indieBeauty;

	@Autowired
	public void setIndieBeauty(IndiebeautyFacade indieBeauty) {
		this.indieBeauty = indieBeauty;
	}
	
	@RequestMapping("/shop/updateCartQuantities.do")
	public ModelAndView handleRequest(
			HttpServletRequest request,	
			@ModelAttribute("sessionCart") Cart cart) throws Exception {
		Iterator<CartItem> cartItems = cart.getAllCartItems();
		while (cartItems.hasNext()) {
			CartItem cartItem = (CartItem) cartItems.next();
			int itemId = cartItem.getItem().getItemId();
			try {
				int quantity = Integer.parseInt(request.getParameter(String.valueOf(itemId)));
				
				//////////////////////////////////////////////				
				if (quantity < 1) {
                    cartItems.remove();
                    cart.removeItemById(itemId);
                }
				
				CartItem ci = cart.setQuantityByItemId(itemId, quantity);
				UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
				if (userSession != null) {  // logged-in            
				    if (quantity < 1) {
				    	indieBeauty.deleteCartItem(ci);
	                }
	                else {
	                	indieBeauty.updateCartItem(ci);
	                }    
				}
				//////////////////////////////////////////////
			}
			catch (NumberFormatException ex) {
				// ignore on purpose
			}
		}
		return new ModelAndView("Cart", "cart", cart);
	}

}
