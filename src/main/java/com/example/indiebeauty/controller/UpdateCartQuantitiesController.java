package com.example.indiebeauty.controller;

import com.example.indiebeauty.repository.CartItemRepository;
import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

@Controller
@SessionAttributes("sessionCart")
public class UpdateCartQuantitiesController {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public UpdateCartQuantitiesController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @RequestMapping("/shop/updateCartQuantities.do")
    public ModelAndView handleRequest(
            HttpServletRequest request,
            @ModelAttribute("sessionCart") Cart cart) throws Exception {
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();
            try {
                int quantity = Integer.parseInt(request.getParameter(itemId));
                cart.setQuantityByItemId(itemId, quantity);

                // Update or delete the item in JPA repository based on quantity
                if (quantity < 1) {
                    cartItems.remove();
                    cartItemRepository.deleteById(cartItem.getId());
                } else {
                    cartItemRepository.save(cartItem); // Update item in JPA repository
                }
            } catch (NumberFormatException ex) {
                // ignore on purpose
            }
        }
        return new ModelAndView("Cart", "cart", cart);
    }

}
