package com.saghar.marketplace.api.controller;

import com.saghar.marketplace.api.dto.CartDtos;
import com.saghar.marketplace.api.service.AppState;
import ir.ac.kntu.model.Product;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final AppState state;

    public CartController(AppState state) {
        this.state = state;
    }

    @GetMapping
    public CartDtos.CartResponse get(Authentication auth) {
        String userKey = (String) auth.getPrincipal();
        return toResponse(userKey);
    }

    @PostMapping("/items")
    public CartDtos.CartResponse add(@Valid @RequestBody CartDtos.AddCartItemRequest req, Authentication auth) {
        String userKey = (String) auth.getPrincipal();
        var cart = state.getOrCreateCart(userKey);
        cart.merge(req.productId(), req.qty(), Integer::sum);
        return toResponse(userKey);
    }

    @DeleteMapping("/items/{productId}")
    public void remove(@PathVariable long productId, Authentication auth) {
        String userKey = (String) auth.getPrincipal();
        var cart = state.getOrCreateCart(userKey);
        cart.remove(productId);
    }

    private CartDtos.CartResponse toResponse(String userKey) {
        var cart = state.getOrCreateCart(userKey);
        List<CartDtos.CartItemResponse> items = new ArrayList<>();
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
        return new CartDtos.CartResponse(userKey, items, total, "IRR");
    }
}
