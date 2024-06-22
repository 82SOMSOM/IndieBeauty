package com.example.indiebeauty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.Orders;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.service.IndiebeautyFacade;
import com.example.indiebeauty.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
			@ModelAttribute("orderForm") OrderForm orderForm, HttpSession session, RedirectAttributes ra)
			throws ModelAndViewDefiningException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (userSession == null) {
			ra.addAttribute("msg", "로그인 후 주문 가능합니다.");
			ra.addAttribute("url", "/login");

			return "redirect:/upload-product/error";
		}

		if (cart != null) {
			// Re-read account from DB at team's request.
			UserInfo userInfo = indiebeauty.getUserInfo(userSession.getUserInfo().getUserid());

			orderForm.getOrder().initOrder(userInfo, cart);
			session.setAttribute("userSession", userSession);
			return "createOrder";
		} else {
			return "redirect:/shop?pageNum=1";
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

	@RequestMapping("/viewAllOrders")
	public ModelAndView getAllOrders(HttpServletRequest request,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (userSession == null || userSession.getUserInfo() == null) {
			return new ModelAndView("Error", "message", "You need to login.");
		}

		String userId = userSession.getUserInfo().getUserid();
		Map<String, Object> resultMap = ordersService.getOrdersByUserId(userId, pageNum);
		@SuppressWarnings("unchecked")
		List<Orders> orders = (List<Orders>)resultMap.get("orders");
		int totalPages = (int) resultMap.get("totalPages");

		ModelAndView mav = new ModelAndView("viewAllOrders");
		mav.addObject("orderList", orders);
		mav.addObject("totalPages", totalPages);
		mav.addObject("currentPage", pageNum);
		return mav;
	}
	

	@RequestMapping("/cancelOrder/{orderId}")
	public String deleteOrder(@PathVariable int orderId, HttpServletRequest request, RedirectAttributes ra)
			throws Exception {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

		if (userSession.getUserInfo().getUserid() != null) {
			ordersService.deleteOrder(orderId);
			return "redirect:/viewAllOrders";
		} else {
			return "redirect:/login";
		}
	}

}
