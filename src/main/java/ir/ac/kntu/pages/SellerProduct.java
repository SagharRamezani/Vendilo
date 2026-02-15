package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.*;

import java.util.List;

public class SellerProduct extends Page {
    private List<Product> products;
    private PageManager pageManager;
    private Seller seller;

    public SellerProduct(PageManager pageManager, Seller seller) {
        super(PageTitle.SELLER_PRODUCT);
        this.pageManager = pageManager;
        this.seller = seller;
        this.products = seller.getProducts();
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Store Products &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.View products\n2.add product\n3.reduce product\n4.increase product\n5.Logout\n6.BACK\n7.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewProducts();
                case "2" -> addProduct();
                case "3" -> reduceProduct();
                case "4" -> increaseProduct();
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

    private void addProduct() throws Exception {
        String type = View.getStringInput("Product Types:\n1.Book\n2.Laptop\n3.Mobile\n Enter Product Type: ").trim();
        switch (type) {
            case "1" -> addBook();
            case "2" -> addLaptop();
            case "3" -> addMobile();
            default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid Product Type" + View.RESET);
        }
        pageManager.back();
    }

    private void reduceProduct() throws Exception {
        Product product = getProduct();
        int quantity = Integer.parseInt(View.getStringInput("Enter quantity: ").trim());
        product.subtractQuantity(seller, quantity);
        System.out.println(View.BRIGHT_GREEN + "Quantity reduced successfully" + View.RESET);
        pageManager.back();
    }

    private void increaseProduct() throws Exception {
        Product product = getProduct();
        int quantity = Integer.parseInt(View.getStringInput("Enter quantity: ").trim());
        product.add(seller, quantity);
        System.out.println(View.BRIGHT_GREEN + "Quantity increased successfully" + View.RESET);
        pageManager.back();
    }

    private Product getProduct() throws Exception {
        String name = View.getStringInput("Enter name: ").trim();
        Product product = null;
        for (Product p : products) {
            if (p.getName().equals(name)) {
                product = p;
            }
        }
        if (product == null) {
            throw new Exception(View.BRIGHT_YELLOW + "Invalid Product Name" + View.RESET);
        }
        return product;
    }

    private void addBook() throws Exception {
        String name = View.getStringInput("Enter name: ").trim();
        double price = Double.parseDouble(View.getStringInput("Enter price: ").trim());
        String author = View.getStringInput("Enter author: ").trim();
        BookGenre genre = BookGenre.valueOf(View.getStringInput("Enter genre: ").trim());
        AgeCategory ageCategory = AgeCategory.valueOf(View.getStringInput("Enter age category: ").trim());
        String isbn = View.getStringInput("Enter isbn: ").trim();
        int pages = Integer.parseInt(View.getStringInput("Enter pages: ").trim());
        Book book = new Book(name, price, author, pages, genre, ageCategory, isbn);
        int quantity = Integer.parseInt(View.getStringInput("Enter quantity: ").trim());
        book.add(seller, quantity);
        this.pageManager.getProductManager().addProduct(book);
    }

    private void addMobile() throws Exception {
        String name = View.getStringInput("Enter name: ").trim();
        double price = Double.parseDouble(View.getStringInput("Enter price: ").trim());
        String brand = View.getStringInput("Enter brand: ").trim();
        int memory = Integer.parseInt(View.getStringInput("Enter internal memory: ").trim());
        int ram = Integer.parseInt(View.getStringInput("Enter ram: ").trim());
        Resolution frontCamera = Resolution.valueOf(View.getStringInput("Enter front camera resolution: ").trim());
        Resolution backCamera = Resolution.valueOf(View.getStringInput("Enter back camera resolution: ").trim());
        NetworkGeneration networkGeneration = NetworkGeneration.valueOf(View.getStringInput("Enter network generation: ").trim());
        Mobile mobile = new Mobile(name, price, brand, memory, ram, frontCamera, backCamera, networkGeneration);
        int quantity = Integer.parseInt(View.getStringInput("Enter quantity: ").trim());
        mobile.add(seller, quantity);
        this.pageManager.getProductManager().addProduct(mobile);
    }

    private void addLaptop() throws Exception {
        String name = View.getStringInput("Enter name: ").trim();
        double price = Double.parseDouble(View.getStringInput("Enter price: ").trim());
        String brand = View.getStringInput("Enter brand: ").trim();
        int memory = Integer.parseInt(View.getStringInput("Enter internal memory: ").trim());
        int ram = Integer.parseInt(View.getStringInput("Enter ram: ").trim());
        boolean bluetooth = Boolean.parseBoolean(View.getStringInput("Has Bluetooth: (true/false) ").trim());
        boolean webcam = Boolean.parseBoolean(View.getStringInput("has webcam: (true/false) ").trim());
        int gpu = Integer.parseInt(View.getStringInput("Enter GPU: ").trim());
        Laptop laptop = new Laptop(name, price, brand, memory, ram, bluetooth, webcam, gpu);
        int quantity = Integer.parseInt(View.getStringInput("Enter quantity: ").trim());
        laptop.add(seller, quantity);
        this.pageManager.getProductManager().addProduct(laptop);
    }

    private void viewProducts() throws Exception {
        System.out.println(View.BRIGHT_CYAN + "&&& Address list &&&" + View.RESET);
        View.extracted(this.products);
        pageManager.back();
    }
}