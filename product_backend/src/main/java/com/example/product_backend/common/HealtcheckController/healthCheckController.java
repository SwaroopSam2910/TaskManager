package com.example.product_backend.common.HealtcheckController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthCheckController {

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
