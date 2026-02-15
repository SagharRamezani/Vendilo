package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Product;

public class Comments extends Page {
    private Customer customer;
    private PageManager pageManager;

    public Comments(PageManager pageManager, Customer customer) {
        super(PageTitle.COMMENTS);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Comments &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.View Comments\n2.addComment\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewComments();
                case "2" -> addComment();
                case "3" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "4" -> pageManager.back();
                case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addComment() throws Exception {
        String name = View.getStringInput("Enter product name: ");
        Product product = this.pageManager.getProductManager().getProductByName(name);
        String comment = View.getStringInput("Enter comment message: ");
        double rate = Double.parseDouble(View.getStringInput("Enter rate:").trim());
        product.rate(this.customer, rate, comment);
        System.out.println(View.BRIGHT_GREEN + "Comment sent." + View.RESET);
        pageManager.back();
    }

    private void viewComments() throws Exception {
        System.out.println(View.BRIGHT_CYAN + "&&& Comments &&&" + View.RESET);
        View.extracted(this.customer.getComments());
        pageManager.back();
    }
}
