package com.example.indiebeauty.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.indiebeauty.domain.Product;

public interface RankRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT p.PRODUCTID, p.PNAME, p.CATEGORYID, p.UDATE, p.PDESCRIPTION, p.PRICE, p.SELLERID, p.STOCK, " +
            "COALESCE(AVG(r.STAR), 0) AS AVERAGE_STAR " +
            "FROM PRODUCT p " +
            "LEFT JOIN REVIEW r ON p.PRODUCTID = r.PRODUCTID " +
            "GROUP BY p.PRODUCTID, p.PNAME, p.CATEGORYID, p.UDATE, p.PDESCRIPTION, p.PRICE, p.SELLERID, p.STOCK " +
            "ORDER BY AVERAGE_STAR DESC", 
    nativeQuery = true)
     Page<Product> findAllOrderByAvgStarDesc(Pageable pageable);
}
