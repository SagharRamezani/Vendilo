package ir.ac.kntu.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private Map<Product, Seller> cart;
    private double total;
    private double shippingCost;
    private double membershipDiscount; // vendilo+ 5%
    private double discount;       // by DiscountCode
    private DiscountCode discountCode;

    public Cart() {
        this.cart = new LinkedHashMap<>();
        this.total = 0;
        this.shippingCost = 45000; // default
        this.membershipDiscount = 0;
        this.discount = 0;
        this.discountCode = null;
    }


    public void setShippingPrice(double price) {
        this.shippingCost = price;
    }

    public double getTotalPrice() {
        return Math.max(0, total - membershipDiscount - discount + shippingCost);
    }

    public void addProduct(Product product, Seller seller) throws Exception {
        if (product.getAmount(seller) == 0) {
            throw new Exception("Seller has no amount");
        }
        product.subtractQuantity(seller, 1);
        this.cart.put(product, seller);
        this.total += product.getPrice();
    }

    public void removeProduct(Product product) {
        this.cart.remove(product);
        this.total -= product.getPrice();
    }

    public Map<Product, Seller> getCart() {
        return cart;
    }

    public String minimize() {
        StringBuilder temp = new StringBuilder();
        for (Product product : this.cart.keySet()) {
            temp.append(product.getName()).append(" ");
        }
        return temp + " -> " + this.total;
    }

    @Override
    public String toString() {
        return minimize();
    }

    public String full() {
        StringBuilder full = new StringBuilder();
        for (Product product : this.cart.keySet()) {
            full.append(product.toString()).append("\n");
        }
        return full.toString();
    }

    public void applyMembership() {
        membershipDiscount = this.total * 0.05;
    }

    public long applyDiscount(DiscountCode discountCode, boolean plus) {
        if (discountCode == null) {
            return 0;
        }
        this.discountCode = discountCode;
        double baseForCode = this.total;
        if (plus) {
            applyMembership();
            baseForCode -= membershipDiscount;
        }
        double d = discountCode.calcDiscount(baseForCode);
        discount = Math.min(d, baseForCode);
        return (long) discount;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }
}
