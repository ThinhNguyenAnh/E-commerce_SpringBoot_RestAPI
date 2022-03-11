package com.app.ecommere.repository;

import com.app.ecommere.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    boolean existsByName(String name);

    @Query(value = "SELECT u FROM Product  u WHERE u.name LIKE %?1%")
    Page<Product> searchProductByName(String keyword , Pageable pageable);

    @Query(value = "SELECT u FROM Product u WHERE u.price >= ?1 AND u.price <= ?2")
    Page<Product> getAllProductByRangePrice(BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    @Query("SELECT u FROM Product u  WHERE u.category.id = ?1")
    Page<Product> getProductByCategory(int categoryId, Pageable pageable);

}
