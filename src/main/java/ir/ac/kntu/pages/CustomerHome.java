package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;

public class CustomerHome extends Page {
    private PageManager pageManager;
    private Customer customer;

    public CustomerHome(PageManager pageManager, Customer customer) {
        super(PageTitle.CUSTOMER_HOME);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            switch (header()) {
                case "1" -> pageManager.navigateToPage(PageTitle.PROFILE, this.customer);
                case "2" -> pageManager.navigateToPage(PageTitle.CART, this.customer);
                case "3" -> pageManager.navigateToPage(PageTitle.STORE, this.customer);
                case "4" -> pageManager.navigateToPage(PageTitle.RECEIPT, this.customer);
                case "5" -> pageManager.navigateToPage(PageTitle.CUSTOMER_REQUEST, this.customer);
                case "6" -> pageManager.navigateToPage(PageTitle.COMMENTS, this.customer);
                case "7" -> pageManager.navigateToPage(PageTitle.NOTIFICATIONS, this.customer);
                case "8" -> pageManager.navigateToPage(PageTitle.PLUS_MEMBERSHIP, this.customer);
                case "9" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "10" -> pageManager.back();
                case "11" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private String header() {
        System.out.println(View.BRIGHT_CYAN + "&&& Customer Home &&&" + View.RESET);
        int notifs = this.customer.unreadNotifications();
        return View.getStringInput(View.BRIGHT_WHITE + "1.Profile\n2.Cart\n3.Store\n4.Receipts\n5.Requests\n6.Comments\n7.Notifictions (" + notifs + ")\n8.Vendilo+\n9.Logout\n10.BACK\n11.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
    }
}