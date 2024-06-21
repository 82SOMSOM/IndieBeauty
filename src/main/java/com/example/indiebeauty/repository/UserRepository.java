package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.indiebeauty.domain.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, String> {
	UserInfo findByUseridAndPasswd(String userId, String password);
	
//	@Query("select u.userid from UserInfo u")
//	List<String> findUserids();
}
