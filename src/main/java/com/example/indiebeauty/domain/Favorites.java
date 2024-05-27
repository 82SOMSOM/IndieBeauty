package com.example.indiebeauty.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Favorites {
	@Id
	private String favId;
}
