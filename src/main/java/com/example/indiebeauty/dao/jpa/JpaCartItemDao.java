package com.example.indiebeauty.dao.jpa;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.example.indiebeauty.dao.CartItemDao;
import com.example.indiebeauty.domain.CartItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * @author Changsup Park
 */
@Repository
public class JpaCartItemDao implements CartItemDao {

	@PersistenceContext
    private EntityManager em;

	public List<CartItem> getCartItemsByUserId(String userId) throws DataAccessException {
		TypedQuery<CartItem> query = em.createQuery(
                "select ci from CartItem ci " +
                "where ci.userId = :id", CartItem.class);
        query.setParameter("id", userId);
        return query.getResultList();
    }
	
	public void insertCartItem(CartItem cartItem) throws DataAccessException {

		TypedQuery<Integer> query = em.createQuery(
                "select max(ci.lineNumber) from CartItem ci", Integer.class);        
        Integer maxLineNum = query.getSingleResult();
        if (maxLineNum == null) {		// no cartitem stored in database
        	cartItem.setLineNumber(0);
        }
        else { 
        	cartItem.setLineNumber(maxLineNum + 1);
        }
        em.persist(cartItem);
    }

	public void updateCartItem(CartItem cartItem) throws DataAccessException {
        em.merge(cartItem);
    }
	
	public void deleteCartItem(CartItem cartItem) throws DataAccessException {
        em.remove(em.merge(cartItem));
    }
}