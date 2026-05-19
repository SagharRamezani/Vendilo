package com.saghar.marketplace.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CartDtos {
    public record AddCartItemRequest(@NotNull Long productId, @Min(1) int qty) {}
    public record CartItemResponse(long itemId, long productId, String name, long price, int qty, long lineTotal) {}
    public record CartResponse(String userKey, List<CartItemResponse> items, long total, String currency) {}
    public record OrderReceiptResponse(long orderId, String userKey, List<CartItemResponse> items, long total, String createdAt) {}
}
