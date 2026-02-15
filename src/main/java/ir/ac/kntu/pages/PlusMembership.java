package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.PlusSubscription;

public class PlusMembership extends Page {
    private final PageManager pageManager;
    private final Customer customer;

    public PlusMembership(PageManager pm, Customer customer) {
        super(PageTitle.PLUS_MEMBERSHIP);
        this.pageManager = pm;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Vendilo+ &&&" + View.RESET);
            var sub = customer.getPlusSubscription();
            boolean active = sub != null && sub.isActive();
            System.out.println("status: " + (active ? "ACTIVE" : "INACTIVE"));
            if (active) {
                System.out.println("expires: " + sub.getExpiresAt());
            }
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Buy 1 month\n2.Buy 3 months\n3.Buy 1 year\n4.Logout\n5.BACK\n6.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> buyDays(30, 90000, active); // sample prices
                case "2" -> buyDays(90, 250000, active);
                case "3" -> buyDays(365, 800000, active);
                case "4" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "5" -> pageManager.back();
                case "6" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void buyDays(int days, double price, boolean active) throws Exception {
        if (customer.getWallet().getBalance() < price) {
            throw new Exception(View.BRIGHT_RED + "Not enough wallet balance" + View.RESET);
        }
        customer.getWallet().withdraw(price);
        if (active) {
            customer.getPlusSubscription().renew(days);
        } else {
            customer.setPlusSubscription(new PlusSubscription(days));
        }
        System.out.println(View.BRIGHT_GREEN + "Vendilo+ activated." + View.RESET);
        pageManager.back();
    }
}
