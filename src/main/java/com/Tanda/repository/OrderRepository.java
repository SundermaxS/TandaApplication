package com.Tanda.repository;

import com.Tanda.entity.Order;
import com.Tanda.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

