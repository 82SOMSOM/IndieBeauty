package com.example.indiebeauty.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.controller.OrderForm;
import com.example.indiebeauty.domain.Category;
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

	public void deleteOrder(int orderId) {
		ordersRepo.deleteById(orderId);
	}
	
	@Transactional
	public void insertOrder(OrderForm orderForm) {
		String userId = orderForm.getUserId();
		Date date = orderForm.getDate();
		int totalPrice = orderForm.getTotalPrice();
		int accountNumber = orderForm.getAccountNumber();
		String address = orderForm.getAddress();
		List<Item> orderItems = orderForm.getOrderItems();
		
		for (int i = 0; i < orderForm.getOrderItems().size(); i++) {
			Item item = (Item) orderForm.getOrderItems().get(i);
			int productId = item.getProduct().getProductId();
			int increment = item.getQuantity();

			Product product = productRepo.getReferenceById(productId); // item entity는 managed 상태
			product.setStock(product.getStock() - increment);
		}
		
		Orders order = new Orders(0, userId, date, totalPrice, 0, accountNumber, address, orderItems);
		Orders newOrder = ordersRepo.save(order);
		
    	for (Item item : orderForm.getOrderItems()) {
            item.setOrder(newOrder);
        	itemRepo.save(item);
        }
	}
}
