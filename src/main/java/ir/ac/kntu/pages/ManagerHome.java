
package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.*;

public class ManagerHome extends Page {
    private final PageManager pageManager;
    private Manager manager;

    public ManagerHome(PageManager pm, Manager manager) {
        super(PageTitle.MANAGER_HOME);
        this.manager = manager;
        this.pageManager = pm;
    }

    private String header() {
        System.out.println(View.BRIGHT_CYAN + "&&& Manager Home &&&" + View.RESET);
        return View.getStringInput(View.BRIGHT_WHITE + "1.Users\n2.Create Public Discount\n3.Broadcast Message\n4.Back\n5.Exit\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
    }

    @Override
    public void show() throws Exception {
        try {
            switch (header()) {
                case "1" -> pageManager.navigateToPage(PageTitle.MANAGE_USERS, this.manager);
                case "2" -> createDiscount();
                case "3" -> broadcast();
                case "4" -> pageManager.back();
                case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void createDiscount() {
        String code = View.getStringInput("Code: ");
        String t = View.getStringInput("Type (1=Fixed, 2=Percent): ");
        DiscountCode dc;
        if ("1".equals(t)) {
            double amt = View.getDouble("Fixed amount: ");
            int max = View.getInt("Max usages: ");
            dc = new DiscountCode(code, amt, max);
        } else {
            int pct = View.getInt("Percent (1-100): ");
            int max = View.getInt("Max usages: ");
            dc = new DiscountCode(code, pct, max);
        }
        pageManager.getUserManager().addGlobalDiscount(dc);
        // notify all customers
        for (Customer c : pageManager.getUserManager().getCustomers()) {
            c.addNotification(new Notification(NotificationType.DISCOUNT, "New discount: " + code, code));
        }
        System.out.println(View.BRIGHT_GREEN + "Created & notified." + View.RESET);
    }

    private void broadcast() {
        String msg = View.getStringInput("Message: ");
        for (Customer c : pageManager.getUserManager().getCustomers()) {
            c.addNotification(new Notification(NotificationType.BROADCAST, msg, ""));
        }
        System.out.println(View.BRIGHT_GREEN + "Broadcast sent." + View.RESET);
    }
}
