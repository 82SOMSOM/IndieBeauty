package com.example.indiebeauty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
}
