package com.c102.ourotest.product.controller;

import com.c102.ourotest.product.dto.CreateProductDTO;
import com.c102.ourotest.product.dto.ProductResponse;
import java.util.UUID;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/{productId}")
    @ApiState(state = State.BUGFIX)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(new ProductResponse(
                "testId",
                "testName",
                12000,
                "testDepart",
                "testMater",
                "testBrand"
        ));
    }

    @PostMapping
    @ApiState(state = State.IMPLEMENTING)
    public ResponseEntity<String> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        return  ResponseEntity.ok(UUID.randomUUID().toString());
    }

}
