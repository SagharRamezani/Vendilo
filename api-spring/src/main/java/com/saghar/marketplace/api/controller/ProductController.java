package com.saghar.marketplace.api.controller;

import com.saghar.marketplace.api.dto.ProductDtos;
import com.saghar.marketplace.api.service.AppState;
import ir.ac.kntu.ProductManager;
import ir.ac.kntu.model.Product;
import ir.ac.kntu.search.ProductQuery;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final AppState state;

    public ProductController(AppState state) {
        this.state = state;
    }

    @GetMapping
    public ProductDtos.PagedResponse<ProductDtos.ProductSummaryResponse> list(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "fuzzy", required = false, defaultValue = "false") boolean fuzzy,
            @RequestParam(name = "sort", required = false, defaultValue = "PRICE_ASC") String sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        ProductManager pm = state.productManager();
        double minP = minPrice == null ? 0 : Math.max(0, minPrice);
        double maxP = maxPrice == null ? Double.MAX_VALUE : Math.max(minP, maxPrice);
        double minS = minRating == null ? 0 : Math.max(0, minRating);

        ProductQuery.SortBy sortBy = ProductQuery.SortBy.PRICE;
        ProductQuery.SortOrder sortOrder = ProductQuery.SortOrder.ASC;
        String s = sort.toUpperCase(Locale.ROOT);
        if (s.contains("DESC")) {
            sortOrder = ProductQuery.SortOrder.DESC;
        }
        if (s.startsWith("NAME")) {
            sortBy = ProductQuery.SortBy.NAME;
        } else if (s.startsWith("RATING") || s.startsWith("SCORE")) {
            sortBy = ProductQuery.SortBy.SCORE;
        }

        ProductQuery query = new ProductQuery(
                q == null ? "" : q,
                type == null ? "" : type,
                minP,
                maxP,
                0,
                sortBy,
                sortOrder,
                fuzzy
        );

        List<Product> filtered = new ArrayList<>(pm.candidates(query));

        if (minS > 0) {
            filtered.removeIf(p -> state.averageRating(findId(p)) < minS);
        }

        Comparator<Product> cmp;
        switch (sortBy) {
            case NAME -> cmp = Comparator.comparing(p -> Optional.ofNullable(p.getName()).orElse(""), String.CASE_INSENSITIVE_ORDER);
            case SCORE -> cmp = Comparator.comparingDouble(p -> state.averageRating(findId(p)));
            default -> cmp = Comparator.comparingDouble(Product::getPrice);
        }
        if (sortOrder == ProductQuery.SortOrder.DESC) {
            cmp = cmp.reversed();
        }
        filtered.sort(cmp);

        int safeSize = Math.max(1, Math.min(100, size));
        int safePage = Math.max(0, page);
        int from = safePage * safeSize;
        int to = Math.min(filtered.size(), from + safeSize);
        List<Product> slice = from >= filtered.size() ? List.of() : filtered.subList(from, to);

        List<ProductDtos.ProductSummaryResponse> items = new ArrayList<>();
        for (Product p : slice) {
            long id = findId(p);
            items.add(summary(id, p));
        }

        long total = filtered.size();
        int totalPages = (int) Math.ceil(total / (double) safeSize);
        return new ProductDtos.PagedResponse<>(items, safePage, safeSize, total, totalPages);
    }

    @GetMapping("/{id}")
    public ProductDtos.ProductDetailsResponse get(@PathVariable long id) {
        Product p = state.findProductById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        return details(id, p);
    }

    @GetMapping("/{id}/reviews")
    public List<ProductDtos.ReviewResponse> reviews(@PathVariable long id) {
        state.findProductById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        return state.reviewsFor(id).stream().map(this::review).toList();
    }

    @PostMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDtos.ReviewResponse createReview(
            @PathVariable long id,
            @Valid @RequestBody ProductDtos.CreateReviewRequest req,
            Authentication authentication
    ) {
        state.findProductById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        String userKey = authentication == null ? "guest" : String.valueOf(authentication.getPrincipal());
        return review(state.addReview(id, userKey, req.rating(), req.comment()));
    }

    private ProductDtos.ProductSummaryResponse summary(long id, Product p) {
        return new ProductDtos.ProductSummaryResponse(
                id,
                p.getName(),
                p.getType(),
                (long) p.getPrice(),
                state.averageRating(id),
                state.reviewCount(id),
                demoCreatedAt(id)
        );
    }

    private ProductDtos.ProductDetailsResponse details(long id, Product p) {
        return new ProductDtos.ProductDetailsResponse(
                id,
                p.getName(),
                p.getType(),
                (long) p.getPrice(),
                p.toString(),
                state.averageRating(id),
                state.reviewCount(id),
                demoCreatedAt(id),
                state.reviewsFor(id).stream().map(this::review).toList()
        );
    }

    private ProductDtos.ReviewResponse review(AppState.ReviewEntry r) {
        return new ProductDtos.ReviewResponse(r.id(), r.productId(), r.userKey(), r.rating(), r.comment(), r.createdAt());
    }

    private Instant demoCreatedAt(long id) {
        return Instant.parse("2025-02-01T00:00:00Z").plusSeconds(id * 86_400L);
    }

    private long findId(Product p) {
        for (var e : state.productById().entrySet()) {
            if (e.getValue() == p) {
                return e.getKey();
            }
        }
        return -1;
    }
}
