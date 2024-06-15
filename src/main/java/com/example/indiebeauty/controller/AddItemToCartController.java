package com.example.indiebeauty.controller;

import com.example.indiebeauty.repository.CartItemRepository;

import jakarta.servlet.http.HttpServletRequest;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;
import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@SessionAttributes("sessionCart")
public class AddItemToCartController {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public AddItemToCartController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @ModelAttribute("sessionCart")
    public Cart createCart() {
        return new Cart();
    }

    @RequestMapping("/shop/addItemToCart.do")
    public ModelAndView handleRequest(
            @RequestParam("workingItemId") String workingItemId,
            @ModelAttribute("sessionCart") Cart cart,
            HttpServletRequest request
    ) throws Exception {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(Long.parseLong(workingItemId));
        if (optionalCartItem.isPresent()) {
            CartItem ci = optionalCartItem.get();
            ci = cart.incrementQuantityByItemId(workingItemId);
            cartItemRepository.save(ci);
        } else {
            boolean isInStock = this.isItemInStock(workingItemId); // Assume you have a method for checking stock
            Item item = this.getItem(workingItemId); // Assume you have a method for getting item details
            CartItem ci = cart.addItem(item, isInStock);
            UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
            if (userSession != null) {    // logged-in
                ci.setUserId(userSession.getAccount().getUsername());
                cartItemRepository.save(ci);
            }
        }
        return new ModelAndView("Cart", "cart", cart);
    }

    // Placeholder methods for isItemInStock and getItem
    private boolean isItemInStock(String itemId) {
        // Implement this method to check stock availability
        return true;
    }

    private Item getItem(String itemId) {
        // Implement this method to get item details
        return new Item();
    }
}
