package com.example.indiebeauty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.service.IndiebeautyFacade;
import com.example.indiebeauty.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({ "sessionCart", "orderForm" })
public class OrdersController {
	@Autowired
	private IndiebeautyFacade indiebeauty;
	
	@Autowired
	private OrdersService ordersService;

	@ModelAttribute("orderForm")
	public OrderForm createOrderForm() {
		return new OrderForm();
	}

	@ModelAttribute("sessionCart")
	public Cart createSessionCart() {
		return new Cart();
	}

	@GetMapping("/newOrder")
	public String initNewOrder(HttpServletRequest request, @ModelAttribute("sessionCart") Cart cart,
			@ModelAttribute("orderForm") OrderForm orderForm) throws ModelAndViewDefiningException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (cart != null) {
			// Re-read account from DB at team's request.
			UserInfo userInfo = indiebeauty.getUserInfo(userSession.getUserInfo().getUserid());
			 // 임시 상품 생성
	        Product product1 = new Product();
	        product1.setProductId(9);
	        product1.setName("Product 1");
	        product1.setPrice(10);
	        product1.setStock(2);

	        // 장바구니 객체 생성
	        Cart carttest = new Cart();

	        // 상품 추가
	        carttest.addProduct(product1, true);
			orderForm.getOrder().initOrder(userInfo, carttest);

			return "createOrder";
		} else {
			ModelAndView modelAndView = new ModelAndView("Error");
			modelAndView.addObject("message", "An order could not be created because a cart could not be found.");
			throw new ModelAndViewDefiningException(modelAndView);
		}
	}

	@PostMapping("/newOrder")
	protected ModelAndView confirmOrder(@ModelAttribute("orderForm") OrderForm orderForm, SessionStatus status) {
		ordersService.insertOrder(orderForm);
		ModelAndView mav = new ModelAndView("checkOrder");
		mav.addObject("order", orderForm);
		mav.addObject("message", "Thank you, your order has been submitted.");
		status.setComplete(); // remove sessionCart and orderForm from session
		return mav;
	}

//	@RequestMapping("/viewOrder")
//	public ModelAndView handleRequest(
//			@ModelAttribute("userSession") UserSession userSession,
//			@RequestParam("orderId") int orderId) throws Exception {
//		Orders order = ordersService.getOrderById(orderId);
//		if (userSession.getAccount().getUsername().equals(order.getUserId())) {
//			return new ModelAndView("ViewOrder", "order", order);
//		} else {
//			return new ModelAndView("Error", "message", "You may only view your own orders.");
//		}
//	}

}
