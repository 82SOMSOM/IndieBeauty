package com.example.indiebeauty.controller;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartProduct;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Iterator;

@Controller
@SessionAttributes("sessionCart")
public class UpdateCartQuantitiesController {

    @ModelAttribute("sessionCart")
    public Cart getCart() {
        return new Cart(); // 세션에 sessionCart 속성이 없는 경우 새로 생성
    }

    @RequestMapping("/shop/updateCartQuantities.do")
    public ModelAndView handleRequest(HttpServletRequest request,
                                      @ModelAttribute("sessionCart") Cart cart) throws Exception {
    	
        Iterator<CartProduct> cartProducts = cart.getAllCartProducts();
        while (cartProducts.hasNext()) {
            CartProduct cartProduct = cartProducts.next();
            int productId = cartProduct.getProduct().getProductId();
            String parameterName = "qty-" + productId;
            
            try {
                int quantity = Integer.parseInt(request.getParameter(parameterName));
                cart.setQuantityByProductId(productId, quantity);
                if (quantity < 1) {
                    cartProducts.remove();
                }
            } catch (NumberFormatException ex) {
                // ignore on purpose
            }
        }
        return new ModelAndView("Cart", "cart", cart);
    }
}
