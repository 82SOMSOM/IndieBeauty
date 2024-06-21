package com.example.indiebeauty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.controller.OrderForm;
import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Orders;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.repository.ItemRepository;
import com.example.indiebeauty.repository.OrdersRepository;
import com.example.indiebeauty.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdersService {
	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private ProductRepository productRepo;

	public Orders getOrderById(int orderId) {
		return ordersRepo.getReferenceById(orderId);
	}

	public List<Orders> getOrdersByUserId(String userId) {
		return ordersRepo.findByUserId(userId);
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

		System.out.println("11111");

		// Create a new Orders object
		Orders order = new Orders(0, userId, date, totalPrice, 0, accountNumber, address, orderItems);

		System.out.println("22" + order.getOrderId());

		Orders newOrder = ordersRepo.saveAndFlush(order);
		for (Item item : order.getOrderItems()) {
			item.setOrderId(newOrder.getOrderId());
			itemRepo.save(item);
		}

		// Iterate through orderItems and save each Item
		for (Item item : orderItems) {
			int productId = item.getProduct().getProductId();
			int increment = item.getQuantity();

			System.out.println("재고 item에 넣은거" + increment);

			Product product = productRepo.getReferenceById(productId); // Fetch the Product entity
			product.setStock(product.getStock() - increment); // Update the stock

			productRepo.save(product); // Save the Item entity
		}

	}

}
