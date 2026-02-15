package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;

public class Welcome extends Page {
    private PageManager pageManager;

    public Welcome(PageManager pageManager) {
        super(PageTitle.WELCOME);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "=== Welcome to Vendilo ===" + View.RESET);
            String choice = View.getStringInput("1.Login\n2.Register\n3.Exit\nEnter your choice: ").trim();
            switch (choice) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void register() throws Exception {
        String choice = View.getStringInput("1.Customer\n2.Seller\nEnter your choice: ").trim();
        switch (choice) {
            case "1" -> pageManager.navigateToPage(PageTitle.CUSTOMER_REGISTER);
            case "2" -> pageManager.navigateToPage(PageTitle.SELLER_REGISTER);
            default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
        }
    }

    private void login() throws Exception {
        String choice = View.getStringInput("1.Customer\n2.Seller\n3.Supporter\n4.Manager\nEnter your choice: ").trim();
        switch (choice) {
            case "1" -> pageManager.navigateToPage(PageTitle.CUSTOMER_LOGIN);
            case "2" -> pageManager.navigateToPage(PageTitle.SELLER_LOGIN);
            case "3" -> pageManager.navigateToPage(PageTitle.SUPPORTER_LOGIN);
            case "4" -> pageManager.navigateToPage(PageTitle.MANAGER_LOGIN);
            default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
        }
    }
}