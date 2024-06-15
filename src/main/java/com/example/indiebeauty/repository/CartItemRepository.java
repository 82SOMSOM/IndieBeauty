package com.example.indiebeauty.repository;

import com.example.indiebeauty.domain.CartItem;
import com.example.indiebeauty.domain.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemPK> {
}
