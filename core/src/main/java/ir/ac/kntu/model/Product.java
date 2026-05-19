package ir.ac.kntu.model;

import ir.ac.kntu.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Product {
    private String name;
    private double price;
    private double quality;
    private double sold;
    private Map<Seller, Integer> sellers;
    private Map<Customer, Comment> ratings;

    public Product(String name, double price) throws Exception {
        this.name = name;
        this.price = price;
        this.quality = 0;
        this.sold = 0;
        this.sellers = new HashMap<>();
        this.ratings = new HashMap<>();
    }

    public List<Seller> sellers() {
        return this.sellers.entrySet().stream().filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public void rate(Customer customer, double score, String comment) throws Exception {
        if (this.ratings.containsKey(customer)) {
            throw new Exception("You already have a rating for " + this.name);
        }
        Comment temp = new Comment(comment, score(score));
        this.ratings.put(customer, temp);
    }

    public int getAmount(Seller seller) throws Exception {
        if (sellers.containsKey(seller)) {
            return sellers.get(seller);
        }
        throw new Exception(View.RED + "Invalid Seller" + View.RESET);
    }

    public double score(double score) {
        if (score > 5) {
            score = 5;
        } else if (score < 0) {
            score = 0;
        }
        this.quality += score;
        this.sold++;
        return score;
    }

    public void add(Seller seller, int amount) {
        if (sellers.containsKey(seller)) {
            sellers.put(seller, sellers.get(seller) + amount);
        } else {
            sellers.put(seller, amount);
        }
    }

    public void subtractQuantity(Seller seller, int amount) throws Exception {
        if (sellers.get(seller) >= amount) {
            sellers.put(seller, sellers.get(seller) - amount);
        } else {
            throw new Exception(View.RED + "Not enough quantity, try another seller" + View.RESET);
        }
    }

    public double getScore() throws Exception {
        if (this.sold == 0) {
            throw new Exception(View.BRIGHT_BLACK + "No review for this product" + View.RESET);
        }
        return this.quality / this.sold;
    }

    public double updatePrice() {
        return price;
    }

    public int getQuantity() {
        int quantity = 0;
        for (Seller seller : sellers.keySet()) {
            quantity += sellers.get(seller);
        }
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getSold() {
        return sold;
    }

    @Override
    public String toString() {
        try {
            return "name: " + name +
                    ", price: " + price +
                    ", score: " + this.getScore() +
                    '}';
        } catch (Exception e) {
            return "name: " + name +
                    ", price: " + price +
                    '}';
        }
    }

    public String getType() {
        return ProductType.PRODUCT.toString();
    }

    public List<Seller> getSellers() {
        List<Seller> onSellers = new ArrayList<>();
        for (Seller seller : this.sellers.keySet()) {
            if (sellers.get(seller) > 0) {
                onSellers.add(seller);
            }
        }
        return onSellers;
    }
}
