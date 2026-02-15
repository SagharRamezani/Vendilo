package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;

public class Profile extends Page {
    private PageManager pageManager;
    private Customer customer;

    public Profile(PageManager pageManager, Customer customer) {
        super(PageTitle.PROFILE);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Profile &&&" + View.RESET);
            System.out.println(View.BRIGHT_WHITE + customer.getFirstName() + customer.getLastName() + "\n" + customer.getEmail() + "\n" + customer.getPhone() + "\n" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_BLUE + "1.Edit information\n2.Addresses\n3.Wallet\n4.Change Password\n5.BACK\n6.EXIT" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> pageManager.navigateToPage(PageTitle.EDIT_PROFILE, this.customer);
                case "2" -> pageManager.navigateToPage(PageTitle.ADDRESSES, this.customer);
                case "3" -> changePass();
                case "4" -> pageManager.navigateToPage(PageTitle.CUSTOMER_WALLET, this.customer);
                case "5" -> pageManager.back();
                case "6" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void changePass() throws Exception {
        String password = View.getStringInput("Enter new password: ");
        customer.setPassword(password);
        pageManager.back();
    }
}