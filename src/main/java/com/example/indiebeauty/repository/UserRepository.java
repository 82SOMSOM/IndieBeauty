package com.example.indiebeauty.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.indiebeauty.domain.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, String> {
	UserInfo findByUseridAndPasswd(String userId, String password);
}
