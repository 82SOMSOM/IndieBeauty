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
import com.example.indiebeauty.service.PetStoreFacade;

/**
 * @author Juergen Hoeller
 * @since 30.11.2003
 * @modified-by Changsup Park
 */
@Controller
@SessionAttributes("sessionCart")
public class RemoveItemFromCartController { 
	private PetStoreFacade petStore;

	@Autowired
	public void setPetStore(PetStoreFacade petStore) {
		this.petStore = petStore;
	}
	
	@RequestMapping("/shop/removeItemFromCart.do")
	public ModelAndView handleRequest(
			@RequestParam("workingItemId") String workingItemId,
			@ModelAttribute("sessionCart") Cart cart
		) throws Exception {
		
		//////////////////////////////////////////////
		CartItem ci = cart.removeItemById(workingItemId);
		if (ci != null) petStore.deleteCartItem(ci);
		//////////////////////////////////////////////
		
		return new ModelAndView("Cart", "cart", cart);
	}
}
