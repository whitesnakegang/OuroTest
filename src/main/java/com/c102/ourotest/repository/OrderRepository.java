package com.c102.ourotest.repository;

import com.c102.ourotest.dto.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    public List<Order> findAll() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 임시 데이터 반환
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("ORD-001", "PROD-001", 2, 199.98));
        orders.add(new Order("ORD-002", "PROD-002", 1, 99.99));
        orders.add(new Order("ORD-003", "PROD-001", 3, 299.97));
        return orders;
    }

    public Order save(Order order) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 임시로 주문 저장 (실제로는 DB에 저장)
        return order;
    }
}

