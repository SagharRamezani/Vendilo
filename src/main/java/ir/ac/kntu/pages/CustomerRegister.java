package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;

public class CustomerRegister extends Page {
    private PageManager pageManager;

    public CustomerRegister(PageManager pageManager) {
        super(PageTitle.CUSTOMER_REGISTER);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Customer Register &&&" + View.RESET);
            String firstName = View.getStringInput("Enter your first name: ").trim();
            String lastName = View.getStringInput("Enter your last name: ").trim();
            String email = View.getStringInput("Enter your email: ").trim();
            String phoneNumber = View.getStringInput("Enter your phone number: ").trim();
            String password = View.getStringInput("Enter your password: ").trim();
            Customer customer = new Customer(firstName, lastName, email, phoneNumber, password);
            pageManager.getUserManager().registerCustomer(customer);
            System.out.println(View.BRIGHT_GREEN + "Wellcom, " + customer.getFirstName() + View.RESET);
            pageManager.navigateToPage(PageTitle.CUSTOMER_HOME, customer);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}
