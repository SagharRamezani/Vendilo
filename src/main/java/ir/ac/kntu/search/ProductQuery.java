package ir.ac.kntu.search;

/**
 * Immutable search DTO for products.
 */
public record ProductQuery(
        String keyword,
        String category,
        double minPrice,
        double maxPrice,
        double minScore,
        SortBy sortBy,
        SortOrder sortOrder,
        boolean useFuzzy
) {

    public enum SortBy {PRICE, SCORE, NAME}

    public enum SortOrder {ASC, DESC}

    public static ProductQuery defaults() {
        return new ProductQuery("", "", 0, Double.MAX_VALUE, 0,
                SortBy.PRICE, SortOrder.ASC, false);
    }
}
