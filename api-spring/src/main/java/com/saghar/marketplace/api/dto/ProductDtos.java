package com.saghar.marketplace.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

public class ProductDtos {
    public record SellerSummary(long id, String storeCode, String name) {}

    public record ProductSummaryResponse(
            long id,
            String name,
            String type,
            long price,
            double avgRating,
            int reviewCount,
            Instant createdAt
    ) {}

    public record PagedResponse<T>(
            List<T> items,
            int page,
            int size,
            long totalItems,
            int totalPages
    ) {}

    public record ProductDetailsResponse(
            long id,
            String name,
            String type,
            long price,
            String description,
            double avgRating,
            int reviewCount,
            Instant createdAt,
            List<ReviewResponse> reviews
    ) {}

    public record ReviewResponse(
            long id,
            long productId,
            String userKey,
            int rating,
            String comment,
            Instant createdAt
    ) {}

    public record CreateReviewRequest(
            @Min(1) @Max(5) int rating,
            @NotBlank String comment
    ) {}
}
