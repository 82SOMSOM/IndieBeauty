package com.example.indiebeauty.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter 
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	@Id
	@Column(name="categoryid")
	private int categoryId;
	private String name;
}
