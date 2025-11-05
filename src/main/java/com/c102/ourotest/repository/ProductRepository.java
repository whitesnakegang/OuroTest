package com.c102.ourotest.repository;

import com.c102.ourotest.dto.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {

    private final Map<String, Product> products;

    public ProductRepository() {
        this.products = new HashMap<>();
        // 임시 데이터 초기화
        products.put("PROD-001", new Product("PROD-001", "Laptop", 999.99, "Electronics"));
        products.put("PROD-002", new Product("PROD-002", "Mouse", 29.99, "Electronics"));
        products.put("PROD-003", new Product("PROD-003", "Keyboard", 79.99, "Electronics"));
    }

    public Product findById(String productId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return products.get(productId);
    }
}

