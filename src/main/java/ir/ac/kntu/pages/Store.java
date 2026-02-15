package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class Store extends Page {
    private PageManager pageManager;

    public Store(PageManager pageManager) {
        super(PageTitle.STORE);
        this.pageManager = pageManager;
    }

    public void viewProducts() throws Exception {
        System.out.println(View.BRIGHT_CYAN + "&&& Products &&&" + View.RESET);
        View.extracted(pageManager.getProductManager().getProducts());
        pageManager.back();
    }

    public void search() throws Exception {
        List<Product> products = pageManager.getProductManager().getProducts();
        System.out.println(View.BRIGHT_MAGENTA + "&&& Search Bar &&&" + View.RESET);
        String name = View.getStringInput("Enter product name: ");
        int minPrice = Integer.parseInt(View.getStringInput("Enter minimum price: "));
        int maxPrice = Integer.parseInt(View.getStringInput("Enter maximum price: "));
        List<Product> filteredProducts = products.stream().filter(p -> p.getName().contains(name)).filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice).collect(Collectors.toList());
        View.extracted(filteredProducts);
        pageManager.back();
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Store &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.View Products\n2.Search\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewProducts();
                case "2" -> search();
                case "3" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "4" -> pageManager.back();
                case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}