package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.Favorites;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
//	List<Favorites> findByUserId(String userId);
}
