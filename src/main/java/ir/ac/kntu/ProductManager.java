package ir.ac.kntu;

import ir.ac.kntu.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductManager {
    private List<Product> products;

    public ProductManager() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public Product getProductByName(String productName) throws Exception {
        for (Product product : this.products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        throw new Exception(View.BRIGHT_YELLOW + "Product not found" + View.RESET);
    }

    public List<Product> searchByName(String q) {
        String needle = q.trim().toLowerCase();
        return products.stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(needle))
                .toList();
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
        List<Product> out = new ArrayList<>();
        if (q == null || q.isBlank()) {
            return out;
        }
        String needle = q.toLowerCase();
        int threshold = Math.max(1, needle.length() / 3);
        for (Product p : products) {
            String name = (p.getName() == null) ? "" : p.getName().toLowerCase();
            if (name.contains(needle) || levenshtein(name, needle) <= threshold) {
                out.add(p);
            }
        }
        return out;
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

    // simple DP
    private static int levenshtein(String a, String b) {
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            d[0][j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1),
                        d[i - 1][j - 1] + cost);
            }
        }
        return d[a.length()][b.length()];
    }
}
