package com.example.indiebeauty.controller;

import java.util.Iterator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("sessionCart")
public class UpdateCartQuantitiesController { 

	@RequestMapping("/shop/updateCartQuantities.do")
	public ModelAndView handleRequest(
			HttpServletRequest request,	
			@ModelAttribute("sessionCart") Cart cart) throws Exception {
		Iterator<CartItem> cartItems = cart.getAllCartItems();
		while (cartItems.hasNext()) {
			CartItem cartItem = (CartItem) cartItems.next();
			int productId = cartItem.getProduct().getProductId();
			String parameterName = Integer.toString(productId); // int 타입의 productid를 String 타입으로 변환
			try {
				int quantity = Integer.parseInt(request.getParameter(parameterName)); // parameterName 사용
                cart.setQuantityByProductId(productId, quantity);
				if (quantity < 1) {
					cartItems.remove();
				}
			}
			catch (NumberFormatException ex) {
				// ignore on purpose
			}
		}
		return new ModelAndView("Cart", "cart", cart);
	}

}
