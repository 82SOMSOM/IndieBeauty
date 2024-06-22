package com.example.indiebeauty.service;

import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.controller.OrderForm;
import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Orders;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.repository.ItemRepository;
import com.example.indiebeauty.repository.OrdersRepository;
import com.example.indiebeauty.repository.ProductRepository;

@Service
public class OrdersService {
	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private ProductRepository productRepo;

	private static final int PAGE_SIZE = 3; 
	
	private Pageable getPageableForOrders(int pageNum) {
		Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "orderId"));
		return (Pageable) PageRequest.of(pageNum, PAGE_SIZE, sort);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getOrdersByUserId(String userId, int pageNum) {
		Pageable pageable = getPageableForOrders(pageNum - 1);
		Page<Orders> result = ordersRepo.findByUserId(userId, pageable);

		int totalPages = result.getTotalPages();
		List<Orders> orders = result.getContent();

		orders.forEach(order -> order.getOrderItems().size());

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("orders", orders);
		resultMap.put("totalPages", totalPages);

		return resultMap;
	}

	public Orders getOrderById(int orderId) {
		return ordersRepo.getReferenceById(orderId);
	}

	@Transactional
	public void deleteOrder(int orderId) {
		Optional<Orders> orderOptional = ordersRepo.findById(orderId);
		if (orderOptional.isPresent()) {
			Orders order = orderOptional.get();
			List<Item> items = order.getOrderItems();
			for (Item item : items) {
				itemRepo.delete(item);
			}
			ordersRepo.delete(order);
		}
	}

	@Transactional
	public void insertOrder(OrderForm orderForm) {
		String userId = orderForm.getOrder().getUserId();
		java.util.Date date = orderForm.getOrder().getDate();
		int totalPrice = orderForm.getOrder().getTotalPrice();
		int accountNumber = orderForm.getOrder().getAccountNumber();
		String address = orderForm.getOrder().getAddress();
		List<Item> orderItems = orderForm.getOrder().getOrderItems();

		Orders order = new Orders(0, userId, date, totalPrice, 0, accountNumber, address, orderItems);


		Orders newOrder = ordersRepo.saveAndFlush(order);
		for (Item item : order.getOrderItems()) {
			item.setOrderId(newOrder.getOrderId());
			itemRepo.save(item);
		}

		for (Item item : orderItems) {
			int productId = item.getProduct().getProductId();
			int increment = item.getQuantity();

			Product product = productRepo.getReferenceById(productId); 
			product.setStock(product.getStock() - increment); 

			productRepo.save(product); 
		}

	}

}
