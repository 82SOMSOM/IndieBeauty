package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class CartItemPK implements Serializable {
	private String userId;
	private int lineNumber;

	public CartItemPK() {
	}

	@Override
	public int hashCode() {
		return Objects.hash(lineNumber, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItemPK other = (CartItemPK) obj;
		return lineNumber == other.lineNumber && Objects.equals(userId, other.userId);
	}

	/*
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		CartItemPK other = (CartItemPK) obj;
		if (lineNumber != other.lineNumber) return false;
		if (orderId != other.orderId) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNumber;
		result = prime * result + orderId;
		return result;
	} */
}
