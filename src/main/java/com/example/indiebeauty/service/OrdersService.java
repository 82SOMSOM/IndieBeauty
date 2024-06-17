package com.example.indiebeauty.service;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.controller.OrderForm;
import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Orders;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.repository.ItemRepository;
import com.example.indiebeauty.repository.OrdersRepository;
import com.example.indiebeauty.repository.ProductRepository;

import ch.qos.logback.classic.Logger;
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

	public void deleteOrder(int orderId) {
		ordersRepo.deleteById(orderId);
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
		
		for(Item item : orderItems) {
			
		}

		// Create a new Orders object
		Orders order = new Orders(0, userId, date, totalPrice, 0, accountNumber, address, orderItems);
		
		System.out.println("22" + order.getOrderId());

		// Save the Orders object to get the managed entity
		System.out.println("333" + order.getOrderItems().get(0).getOrder().getOrderId());

		Orders newOrder = ordersRepo.save(order);
		System.out.println("777" + newOrder.getOrderItems().get(0).getOrder().getOrderId());
/*		
		// Iterate through orderItems and save each Item
		for (Item item : orderItems) {
			int productId = item.getProduct().getProductId();
			int increment = item.getQuantity();

			Product product = productRepo.getReferenceById(productId); // Fetch the Product entity
			product.setStock(product.getStock() - increment); // Update the stock
			
			
			item.setOrder(newOrder);
			
			System.out.println("4444" + item.getOrder().getOrderId() + newOrder.getOrderId());

//			productRepo.save(product); // Save the Item entity
		}
*/
	}

}
