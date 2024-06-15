package com.example.indiebeauty.repository;

import com.example.indiebeauty.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
