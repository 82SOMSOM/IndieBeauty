package com.example.indiebeauty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> getCategoryList() {
		System.out.println("categoryRepository : ");
		List<Category> list = categoryRepository.findAll();
		for (Category c : list) {
			System.out.println(c);
		}
		return categoryRepository.findAll();
	}
}
