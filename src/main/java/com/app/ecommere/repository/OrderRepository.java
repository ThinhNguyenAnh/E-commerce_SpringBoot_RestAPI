package com.app.ecommere.repository;

import com.app.ecommere.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.Order;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
}
