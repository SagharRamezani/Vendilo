package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.RequestType;

public class CustomerRequest extends Page {
    private Customer customer;
    private PageManager pageManager;

    public CustomerRequest(PageManager pageManager, Customer customer) {
        super(PageTitle.CUSTOMER_REQUEST);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Requests &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.View Requests\n2.Add Request\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewRequests();
                case "2" -> addRequest();
                case "3" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "4" -> pageManager.back();
                case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void addRequest() throws Exception {
        RequestType subject = chooseSubject();
        String request = View.getStringInput("Enter Request: ");
        this.pageManager.getUserManager().request(this.customer, subject, request);
        System.out.println(View.BRIGHT_GREEN + "Request successfully sent." + View.RESET);
        pageManager.back();
    }

    private RequestType chooseSubject() throws Exception {
        String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Settings\n2.Order Not Received\n3.Quality Issue\n4.Order Mismatch\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
        return switch (choice) {
            case "1" -> RequestType.SETTINGS;
            case "2" -> RequestType.ORDER_NOT_RECEIVED;
            case "3" -> RequestType.QUALITY_ISSUE;
            case "4" -> RequestType.ORDER_MISMATCH;
            default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
        };
    }

    private void viewRequests() throws Exception {
        System.out.println(View.BRIGHT_MAGENTA + "Requests" + View.RESET);
        View.extracted(this.customer.getRequests());
        pageManager.back();
    }
}