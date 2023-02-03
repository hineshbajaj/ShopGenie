package com.hinesh.shopgenie.order.repository;

import com.hinesh.shopgenie.order.domain.Cart;
import com.hinesh.shopgenie.order.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByStatus(CartStatus status);

    List<Cart> findByStatusAndCustomerId(CartStatus status, Long customerId);
}
        