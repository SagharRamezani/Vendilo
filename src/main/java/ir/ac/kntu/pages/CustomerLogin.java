package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.Verify;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;

public class CustomerLogin extends Page {
    private PageManager pageManager;

    public CustomerLogin(PageManager pageManager) {
        super(PageTitle.CUSTOMER_LOGIN);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Customer Login &&&" + View.RESET);
            String choice = View.getStringInput("Enter with: " + View.BRIGHT_BLACK + "1.email or 2.phone or 3.BACK" + View.RESET).trim();
            Customer customer;
            String input = getString(choice);
            String pass = View.getStringInput("Enter your password: ").trim();
            switch (choice) {
                case "1" -> customer = pageManager.getUserManager().logInCustomerE(input, pass);
                case "2" -> customer = pageManager.getUserManager().logInCustomerP(input, pass);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
            }
            System.out.println(View.BRIGHT_GREEN + "Welcome, " + customer.getFirstName() + View.RESET);
            pageManager.navigateToPage(PageTitle.CUSTOMER_HOME, customer);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private String getString(String choice) throws Exception {
        String input = "";
        switch (choice) {
            case "1":
                input = View.getStringInput("Enter your email address: ").toLowerCase().trim();
                Verify.verifyEmail(input);
                break;
            case "2":
                input = View.getStringInput("Enter your phone number: ").trim();
                Verify.verifyPhone(input);
                break;
            case "3":
                pageManager.back();
                break;
            default:
                throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
        }
        return input;
    }
}