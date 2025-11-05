package com.c102.ourotest.service;

import com.c102.ourotest.dto.Order;
import com.c102.ourotest.dto.OrderCreateRequest;
import com.c102.ourotest.dto.Product;
import com.c102.ourotest.repository.OrderRepository;
import com.c102.ourotest.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(OrderCreateRequest request, Boolean applyDiscount,
            Double discountRate) {

        try {
            test1();
            test2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 첫 번째 repository 호출: ProductRepository에서 상품 정보 조회
        Product product = productRepository.findById(request.getProductId());

        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + request.getProductId());
        }

        // 가격 계산
        Double basePrice = product.getPrice() * request.getQuantity();
        Double totalPrice = basePrice;

        // 할인 적용
        if (applyDiscount != null && applyDiscount && discountRate != null) {
            totalPrice = basePrice * (1 - discountRate);
        }

        // 주문 생성
        String orderId = "ORD-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        Order order = new Order(orderId, request.getProductId(), request.getQuantity(), totalPrice);


        // 두 번째 repository 호출: OrderRepository에 주문 저장
        return orderRepository.save(order);
    }

    public void test1() throws Exception {
        Thread.sleep(1000);
        test3();
    }

    public void test2() throws Exception {
        Thread.sleep(2000);
    }

    private void test3() throws Exception {
        Thread.sleep(3000);
    }
}

