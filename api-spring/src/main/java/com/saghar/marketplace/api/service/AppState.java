package com.saghar.marketplace.api.service;

import ir.ac.kntu.ProductManager;
import ir.ac.kntu.UserManager;
import ir.ac.kntu.model.*;
import ir.ac.kntu.util.SampleData;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class AppState {
    private final UserManager userManager = new UserManager();
    private final ProductManager productManager = new ProductManager();

    private final Map<Long, Product> productById = new HashMap<>();
    private final Map<Long, List<ReviewEntry>> reviewsByProductId = new HashMap<>();
    private long reviewSeq = 1000;

    // userKey -> (productId -> qty)
    private final Map<String, LinkedHashMap<Long, Integer>> carts = new HashMap<>();

    public AppState() {
        try {
            for (User u : SampleData.users()) {
                if (u instanceof Customer c) {
                    userManager.addCustomer(c);
                } else if (u instanceof Seller s) {
                    userManager.addSeller(s);
                } else if (u instanceof Supporter sp) {
                    userManager.addSupporter(sp);
                } else if (u instanceof Manager m) {
                    userManager.addManager(m);
                }
            }
            long id = 1;
            for (Product p : SampleData.products()) {
                productManager.addProduct(p);
                productById.put(id++, p);
            }
            seedReviews();
        } catch (Exception e) {
            // Don't crash the whole API if sample data fails validation.
            // This keeps the service runnable in all environments.
            System.err.println("[WARN] Sample data initialization failed. Starting with empty state.");
            e.printStackTrace();
        }
    }

    private void seedReviews() {
        addReview(1L, "ali@mail.com", 5, "A charming classic with a memorable story.");
        addReview(1L, "sara@mail.com", 4, "Beautiful and short; good for a quick read.");
        addReview(6L, "mina@mail.com", 5, "A must-read for clean software design.");
        addReview(6L, "ali@mail.com", 4, "Very practical examples for refactoring.");
        addReview(7L, "sara@mail.com", 5, "Still one of the best Java books.");
        addReview(10L, "ali@mail.com", 4, "Fast and reliable laptop for daily work.");
        addReview(20L, "mina@mail.com", 5, "Excellent phone, premium feel and camera.");
    }

    public UserManager userManager() { return userManager; }

    public ProductManager productManager() { return productManager; }

    public Optional<Product> findProductById(long id) { return Optional.ofNullable(productById.get(id)); }

    public Map<Long, Product> productById() { return Collections.unmodifiableMap(productById); }

    public LinkedHashMap<Long, Integer> getOrCreateCart(String userKey) {
        return carts.computeIfAbsent(userKey, k -> new LinkedHashMap<>());
    }

    public void clearCart(String userKey) { carts.remove(userKey); }

    public synchronized ReviewEntry addReview(long productId, String userKey, int rating, String comment) {
        ReviewEntry entry = new ReviewEntry(
                ++reviewSeq,
                productId,
                userKey == null || userKey.isBlank() ? "guest" : userKey,
                Math.max(1, Math.min(5, rating)),
                comment == null ? "" : comment.trim(),
                Instant.now()
        );
        reviewsByProductId.computeIfAbsent(productId, ignored -> new ArrayList<>()).add(0, entry);
        return entry;
    }

    public synchronized List<ReviewEntry> reviewsFor(long productId) {
        return List.copyOf(reviewsByProductId.getOrDefault(productId, List.of()));
    }

    public synchronized double averageRating(long productId) {
        List<ReviewEntry> reviews = reviewsByProductId.getOrDefault(productId, List.of());
        if (reviews.isEmpty()) {
            return 0;
        }
        return reviews.stream().mapToInt(ReviewEntry::rating).average().orElse(0);
    }

    public synchronized int reviewCount(long productId) {
        return reviewsByProductId.getOrDefault(productId, List.of()).size();
    }

    public record ReviewEntry(long id, long productId, String userKey, int rating, String comment, Instant createdAt) {}
}
