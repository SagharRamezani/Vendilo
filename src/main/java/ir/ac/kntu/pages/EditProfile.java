package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;

public class EditProfile extends Page {
    private PageManager pageManager;
    private Customer customer;

    public EditProfile(PageManager pageManager, Customer customer) {
        super(PageTitle.EDIT_PROFILE);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Edit information &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_BLUE + "1.Edit Email\n2.Phone\n3.First Name\n4.Last Name\n5.BACK\n6.EXIT" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "5" -> pageManager.back();
                case "6" -> pageManager.navigateToPage(PageTitle.EXIT);
                case "1" -> editEmail();
                case "2" -> editPhone();
                case "3" -> editFName();
                case "4" -> editLName();
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void editLName() throws Exception {
        this.customer.setLastName(View.getStringInput("Enter new Last Name: "));
        pageManager.back();
    }

    private void editFName() throws Exception {
        this.customer.setFirstName(View.getStringInput("Enter new First Name: "));
        pageManager.back();
    }

    private void editEmail() throws Exception {
        String email = View.getStringInput("Enter new email address: ");
        this.pageManager.getUserManager().verifyEmail(email);
        this.customer.setEmail(email);
        pageManager.back();
    }

    private void editPhone() throws Exception {
        String phone = View.getStringInput("Enter new phone number: ");
        this.pageManager.getUserManager().verifyPhone(phone);
        this.customer.setPhone(phone);
        pageManager.back();
    }
}