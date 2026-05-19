package com.saghar.marketplace.api.controller;

import com.saghar.marketplace.api.dto.CartDtos;
import com.saghar.marketplace.api.service.AppState;
import ir.ac.kntu.model.Product;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final AppState state;
    private long orderSeq = 3000;

    public OrderController(AppState state) {
        this.state = state;
    }

    @PostMapping("/checkout")
    public CartDtos.OrderReceiptResponse checkout(Authentication auth) {
        String userKey = (String) auth.getPrincipal();
        var cart = state.getOrCreateCart(userKey);

        var items = new ArrayList<CartDtos.CartItemResponse>();
        long total = 0;
        long itemId = 1;
        for (var e : cart.entrySet()) {
            long pid = e.getKey();
            int qty = e.getValue();
            Product p = state.findProductById(pid).orElse(null);
            String name = p == null ? "Unknown" : p.getName();
            long price = p == null ? 0 : (long) p.getPrice();
            long line = price * (long) qty;
            total += line;
            items.add(new CartDtos.CartItemResponse(itemId++, pid, name, price, qty, line));
        }

        long orderId = ++orderSeq;
        state.clearCart(userKey);
        return new CartDtos.OrderReceiptResponse(orderId, userKey, items, total, Instant.now().toString());
    }
}
