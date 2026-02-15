package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.Verify;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Receipt;

import java.util.List;

public class SupporterReceipt extends Page {
    private PageManager pageManager;

    public SupporterReceipt(PageManager pageManager) {
        super(PageTitle.SUPPORTER_RECEIPT);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Search Receipt &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.by Seller\n2.by Customer\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> bySeller();
                case "2" -> byCustomer();
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

    private void bySeller() throws Exception {
        String storeName = View.getStringInput("Enter Store Name: ");
        List<Receipt> receipts = pageManager.getUserManager().getSeller(storeName).getReceipts();
        View.extracted(receipts);
        pageManager.back();
    }

    private void byCustomer() throws Exception {
        String choice = View.getStringInput("1.Email or 2.Phone: ");
        Customer customer;
        switch (choice) {
            case "1":
                String email = View.getStringInput("Enter Email: ");
                Verify.verifyEmail(email);
                customer = pageManager.getUserManager().getCustomerE(email);
                break;
            case "2":
                String phone = View.getStringInput("Enter Phone Number: ");
                Verify.verifyPhone(phone);
                customer = pageManager.getUserManager().getCustomerP(phone);
                break;
            default:
                throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
        }
        List<Receipt> receipts = customer.getReceipts();
        View.extracted(receipts);
        pageManager.back();
    }
}
