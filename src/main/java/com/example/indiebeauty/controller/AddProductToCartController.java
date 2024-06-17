package com.example.indiebeauty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.service.IndiebeautyFacade;

@Controller
@SessionAttributes("sessionCart")
public class AddProductToCartController { 

	private IndiebeautyFacade indiebeauty;

	@Autowired
	public void setIndiebeauty(IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}

	@ModelAttribute("sessionCart")
	public Cart createCart() {
		return new Cart();
	}
	
	@RequestMapping("/shop/addProductToCart.do")
	public ModelAndView handleRequest(
			@RequestParam("workingProductId") int workingProductId,
			@ModelAttribute("sessionCart") Cart cart 
			) throws Exception {
		if (cart.containsProductId(workingProductId)) {
			cart.incrementQuantityByProductId(workingProductId);
		}
		else {
			// isInStock is a "real-time" property that must be updated
			// every time an item is added to the cart, even if other
			// item details are cached.
			boolean isInStock = this.indiebeauty.isProductInStock(workingProductId);
			Product product = this.indiebeauty.getProduct(workingProductId);
			cart.addProduct(product, isInStock);
		}
		return new ModelAndView("Cart", "cart", cart);
	}
}