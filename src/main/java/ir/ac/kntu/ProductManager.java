package ir.ac.kntu;

import ir.ac.kntu.model.Product;
import ir.ac.kntu.util.BKTree;

import java.util.*;

public class ProductManager {
    private List<Product> products;

    // indices for faster queries
    private final Map<String, Product> byExactNameLower = new HashMap<>();
    private final Map<String, List<Product>> byTypeLower = new HashMap<>();
    private final NavigableMap<Long, List<Product>> byPrice = new TreeMap<>();

    // Fuzzy index (built lazily)
    private BKTree bkTree;
    private final Map<String, List<Product>> nameToProducts = new HashMap<>();
    private boolean fuzzyDirty = true;

    public ProductManager() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        this.products.add(product);
        indexProduct(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
        deindexProduct(product);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public Product getProductByName(String productName) throws Exception {
        if (productName != null) {
            Product p = byExactNameLower.get(productName.trim().toLowerCase());
            if (p != null) {
                return p;
            }
        }
        throw new Exception(View.BRIGHT_YELLOW + "Product not found" + View.RESET);
    }

    public List<Product> searchByName(String q) {
        return byNameContains(q);
    }

    public List<Product> sortByPrice(boolean asc) {
        return products.stream()
                .sorted(asc ? Comparator.comparing(Product::getPrice)
                        : Comparator.comparing(Product::getPrice).reversed())
                .toList();
    }

    // contains search
    public List<Product> byNameContains(String q) {
        List<Product> out = new ArrayList<>();
        if (q == null || q.isBlank()) {
            return out;
        }
        String needle = q.trim().toLowerCase();
        for (Product p : products) {
            String n = (p.getName() == null) ? "" : p.getName().toLowerCase();
            if (n.contains(needle)) {
                out.add(p);
            }
        }
        return out;
    }

    // fuzzy search (Levenshtein threshold = len/3)
    public List<Product> fuzzyByName(String q) {
        if (q == null || q.isBlank()) {
            return List.of();
        }
        ensureBKTree();
        String needle = q.trim().toLowerCase();
        int threshold = Math.max(1, needle.length() / 3);
        List<String> terms = bkTree.search(needle, threshold);
        List<Product> out = new ArrayList<>();
        for (String t : terms) {
            List<Product> ps = nameToProducts.get(t);
            if (ps != null) {
                out.addAll(ps);
            }
        }
        // Also include substring matches (cheap) for nicer UX.
        for (Product p : byNameContains(needle)) {
            if (!out.contains(p)) {
                out.add(p);
            }
        }
        return out;
    }

    /**
     * Candidate set selection using indices.
     */
    public Collection<Product> candidates(ir.ac.kntu.search.ProductQuery q) {
        if (q == null) {
            return products;
        }

        // 1) Keyword-based candidate set (optional)
        List<Product> keywordCandidates;
        if (q.keyword() != null && !q.keyword().isBlank()) {
            keywordCandidates = q.useFuzzy() ? fuzzyByName(q.keyword()) : byNameContains(q.keyword());
        } else {
            keywordCandidates = new ArrayList<>(products);
        }

        // 2) Category-based narrowing (optional)
        if (q.category() != null && !q.category().isBlank()) {
            List<Product> byType = byTypeLower.getOrDefault(q.category().trim().toLowerCase(), List.of());
            // intersection
            if (keywordCandidates.size() <= byType.size()) {
                return keywordCandidates.stream().filter(byType::contains).toList();
            }
            return byType.stream().filter(keywordCandidates::contains).toList();
        }

        // 3) Price-based narrowing (optional)
        if (q.minPrice() > 0 || q.maxPrice() < Double.MAX_VALUE) {
            long lo = (long) Math.floor(q.minPrice());
            long hi = (long) Math.ceil(q.maxPrice());
            List<Product> pr = new ArrayList<>();
            for (List<Product> bucket : byPrice.subMap(lo, true, hi, true).values()) {
                pr.addAll(bucket);
            }
            if (keywordCandidates.size() <= pr.size()) {
                return keywordCandidates.stream().filter(pr::contains).toList();
            }
            return pr.stream().filter(keywordCandidates::contains).toList();
        }

        return keywordCandidates;
    }

    public List<Product> search(String q, String cat, double minP, double maxP) {
        List<Product> out = new ArrayList<>(); // result
        for (Product p : this.products) {
            // keyword
            if (q != null && !q.isEmpty()) {
                String name = p.getName() == null ? "" : p.getName().toLowerCase();
                String kw = q.toLowerCase();
                if (!name.contains(kw)) {
                    continue;
                }
            }
            // category
            if (cat != null && !cat.isEmpty()) {
                if (p.getType() == null || !p.getType().equalsIgnoreCase(cat)) {
                    continue;
                }
            }
            // price range
            double price = p.getPrice();
            if (price < minP || price > maxP) {
                continue;
            }
            out.add(p);
        }
        return out;
    }


//    // combined filter
//    public List<Product> filter(String nameQ, String type,
//                                Long minPrice, Long maxPrice,
//                                Double minRating, boolean ascPrice) {
//        List<Product> out = new ArrayList<>();
//        for (Product p : products) {
//            if (nameQ != null && !nameQ.isBlank()) {
//                String n = (p.getName() == null) ? "" : p.getName().toLowerCase();
//                if (!n.contains(nameQ.toLowerCase())) continue;
//            }
//            if (type != null && !type.isBlank()) {
//                if (p.getType() == null || !p.getType().equalsIgnoreCase(type)) continue;
//            }
//            if (minPrice != null && p.getPrice() < minPrice) continue;
//            if (maxPrice != null && p.getPrice() > maxPrice) continue;
//            if (minRating != null && p.getAverageRating() < minRating) continue;
//            out.add(p);
//        }
//        out.sort(ascPrice
//                ? Comparator.comparingLong(Product::getPrice)
//                : Comparator.comparingLong(Product::getPrice).reversed());
//        return out;
//    }


    private void indexProduct(Product p) {
        if (p == null) {
            return;
        }
        if (p.getName() != null) {
            byExactNameLower.put(p.getName().trim().toLowerCase(), p);
            fuzzyDirty = true;
        }
        if (p.getType() != null) {
            String t = p.getType().trim().toLowerCase();
            byTypeLower.computeIfAbsent(t, k -> new ArrayList<>()).add(p);
        }
        long priceKey = (long) p.getPrice();
        byPrice.computeIfAbsent(priceKey, k -> new ArrayList<>()).add(p);
    }

    private void deindexProduct(Product p) {
        if (p == null) {
            return;
        }
        if (p.getName() != null) {
            byExactNameLower.remove(p.getName().trim().toLowerCase());
            fuzzyDirty = true;
        }
        if (p.getType() != null) {
            String t = p.getType().trim().toLowerCase();
            List<Product> list = byTypeLower.get(t);
            if (list != null) {
                list.remove(p);
                if (list.isEmpty()) {
                    byTypeLower.remove(t);
                }
            }
        }
        long priceKey = (long) p.getPrice();
        List<Product> bucket = byPrice.get(priceKey);
        if (bucket != null) {
            bucket.remove(p);
            if (bucket.isEmpty()) {
                byPrice.remove(priceKey);
            }
        }
    }

    private void ensureBKTree() {
        if (!fuzzyDirty && bkTree != null) {
            return;
        }
        bkTree = new BKTree();
        nameToProducts.clear();
        for (Product p : products) {
            if (p.getName() == null) {
                continue;
            }
            String t = p.getName().trim().toLowerCase();
            bkTree.add(t);
            nameToProducts.computeIfAbsent(t, k -> new ArrayList<>()).add(p);
        }
        fuzzyDirty = false;
    }
}
