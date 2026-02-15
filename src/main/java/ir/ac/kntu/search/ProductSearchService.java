package ir.ac.kntu.search;

import ir.ac.kntu.ProductManager;
import ir.ac.kntu.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Search pipeline: candidate selection -> filtering -> sorting.
 */
public final class ProductSearchService {
    private final ProductManager pm;

    public ProductSearchService(ProductManager pm) {
        this.pm = pm;
    }

    public List<Product> search(ProductQuery q) {
        if (q == null) {
            q = ProductQuery.defaults();
        }
        List<Product> candidates = new ArrayList<>(pm.candidates(q));

        // filter stage
        List<Product> filtered = new ArrayList<>();
        for (Product p : candidates) {
            if (!passesFilters(p, q)) {
                continue;
            }
            filtered.add(p);
        }

        // sort stage
        filtered.sort(comparator(q));
        return filtered;
    }

    private boolean passesFilters(Product p, ProductQuery q) {
        double price = p.getPrice();
        if (price < q.minPrice() || price > q.maxPrice()) {
            return false;
        }

        if (q.category() != null && !q.category().isBlank()) {
            String type = p.getType() == null ? "" : p.getType();
            if (!type.equalsIgnoreCase(q.category().trim())) {
                return false;
            }
        }

        double score;
        try {
            score = p.getScore();
        } catch (Exception e) {
            score = 0; // "no review" treated as 0
        }
        return score >= q.minScore();
    }

    private Comparator<Product> comparator(ProductQuery q) {
        Comparator<Product> cmp;
        switch (q.sortBy()) {
            case NAME ->
                    cmp = Comparator.comparing(p -> (p.getName() == null ? "" : p.getName()), String.CASE_INSENSITIVE_ORDER);
            case SCORE -> cmp = Comparator.comparingDouble(p -> {
                try {
                    return p.getScore();
                } catch (Exception e) {
                    return 0;
                }
            });
            case PRICE -> cmp = Comparator.comparingDouble(Product::getPrice);
            default -> cmp = Comparator.comparingDouble(Product::getPrice);
        }
        if (q.sortOrder() == ProductQuery.SortOrder.DESC) {
            return cmp.reversed();
        }
        return cmp;
    }
}
