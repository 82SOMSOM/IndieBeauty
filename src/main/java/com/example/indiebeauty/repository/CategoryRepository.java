package com.example.indiebeauty.repository;

import org.springframework.stereotype.Repository;

import com.example.indiebeauty.domain.Category;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}