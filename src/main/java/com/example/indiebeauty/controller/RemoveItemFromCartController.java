package com.example.indiebeauty.controller;

import com.example.indiebeauty.repository.CartItemRepository;
import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("sessionCart")
public class RemoveItemFromCartController {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public RemoveItemFromCartController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @RequestMapping("/shop/removeItemFromCart.do")
    public ModelAndView handleRequest(
            @RequestParam("workingItemId") String workingItemId,
            @ModelAttribute("sessionCart") Cart cart
    ) throws Exception {
        cart.removeItemById(workingItemId); // Assuming this method is defined in Cart class
        cartItemRepository.deleteById(Long.parseLong(workingItemId)); // Delete from JPA repository
        return new ModelAndView("Cart", "cart", cart);
    }
}
