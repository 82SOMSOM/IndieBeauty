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
import com.example.indiebeauty.service.ProductService;

@Controller
@SessionAttributes("sessionCart")
public class AddProductToCartController { 

	private IndiebeautyFacade indiebeauty;

	@Autowired
	public void setIndiebeauty(IndiebeautyFacade indiebeauty) {
		this.indiebeauty = indiebeauty;
	}
	
	@Autowired
	private ProductService productService;

	@ModelAttribute("sessionCart")
	public Cart createCart() {
		return new Cart();
	}
	
	@RequestMapping("/shop/addProductToCart.do")
	public ModelAndView handleRequest(
			@RequestParam("workingProductId") int workingProductId,
			@RequestParam("quantity") int quantity,
			@ModelAttribute("sessionCart") Cart cart 
			) throws Exception {
		if (cart.containsProductId(workingProductId)) {
			cart.incrementQuantityByProductId(workingProductId, quantity);
		}
		else {
			boolean isInStock = this.indiebeauty.isProductInStock(workingProductId);
			Product product = productService.getProductById(workingProductId);
			cart.addProduct(product, isInStock, quantity);
		}
		return new ModelAndView("Cart", "cart", cart);
	}
}