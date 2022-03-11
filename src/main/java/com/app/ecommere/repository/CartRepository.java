package com.app.ecommere.repository;

import com.app.ecommere.entity.Cart;
import com.app.ecommere.entity.Product;
import com.app.ecommere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {


    List<Cart> findAllByUserId(Integer userId);

    Cart findByProductAndUser( Product product, User user);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantity = ?2 WHERE c.product.id = ?1 AND c.user.id = ?3")
    void updateQuantity(Integer productId, Integer quantity, Integer customerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.product.id = ?1 AND c.user.id = ?2")
    void removeCart(Integer productId, Integer customerId);
}
