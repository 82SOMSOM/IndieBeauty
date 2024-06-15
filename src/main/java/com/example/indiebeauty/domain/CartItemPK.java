package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.Objects;

public class CartItemPK implements Serializable {

    private String userId;
    private int lineNumber;

    // Getters and setters, equals(), hashCode() 등 필요한 메서드 추가

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemPK that = (CartItemPK) o;
        return lineNumber == that.lineNumber &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, lineNumber);
    }
}
