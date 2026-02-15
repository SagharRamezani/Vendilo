package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Product;
import ir.ac.kntu.model.Seller;

import java.util.List;

public class CustomerCart extends Page {
    private PageManager pageManager;
    private Customer customer;

    public CustomerCart(PageManager pageManager, Customer customer) {
        super(PageTitle.CART);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    public void addProduct() throws Exception {
        String name = View.getStringInput("Enter Product Name: ");
        Product product = pageManager.getProductManager().getProductByName(name);
        List<Seller> sellers = product.sellers();
        View.extracted(sellers);
        String storeName = View.getStringInput("Enter Store Name: ");
        Seller seller = this.pageManager.getUserManager().getSeller(storeName);
        this.customer.getCart().addProduct(product, seller);
        pageManager.back();
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Cart &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.ViewCart\n2.Add Product\n3.Remove Product\n4.Buy\n5.Logout\n6.BACK\n7.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewCart();
                case "2" -> addProduct();
                case "3" -> removeProduct();
                case "4" -> buy();
                case "5" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "6" -> pageManager.back();
                case "7" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void viewCart() throws Exception {
        customer.getCart().minimize();
        pageManager.back();
    }

    private void buy() throws Exception {
        String title = View.getStringInput("Enter Address title: ");
        Address address = this.customer.getAddress(title);
        String code = View.getStringInput("Discount code (empty to skip): ").trim();
        if (!code.isEmpty()) {
            var opt = pageManager.getUserManager().getGlobalDiscounts().stream().filter(d -> d.getCode().equalsIgnoreCase(code)).findFirst();
            if (opt.isPresent()) {
                long applied = this.customer.getCart().applyDiscount(opt.get(), this.customer.isPlus());
                System.out.println(View.BRIGHT_GREEN + "Discount applied: " + applied + View.RESET);
            } else {
                System.out.println(View.BRIGHT_YELLOW + "Invalid code" + View.RESET);
            }
        } else if (customer.isPlus()) {
            customer.getCart().applyMembership();
        }
        this.customer.buy(address);
        pageManager.back();
    }


    private void removeProduct() throws Exception {
        String name = View.getStringInput("Enter Product Name: ");
        Product product = pageManager.getProductManager().getProductByName(name);
        this.customer.getCart().removeProduct(product);
        pageManager.back();
    }
}