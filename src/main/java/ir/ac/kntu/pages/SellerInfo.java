package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;

public class SellerInfo extends Page {
    private PageManager pageManager;
    private Seller seller;

    public SellerInfo(PageManager pageManager, Seller seller) {
        super(PageTitle.SELLER_INFO);
        this.pageManager = pageManager;
        this.seller = seller;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Edit Info &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Edit first name\n2.Edit last name\n3.Edit store name\n4.Edit phone number\n5.Edit store state\n6.Logout\n7.BACK\n8.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> editFirstName();
                case "2" -> editLastName();
                case "3" -> editStoreName();
                case "4" -> editPhone();
                case "5" -> editStoreState();
                case "6" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "7" -> pageManager.back();
                case "8" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void editFirstName() {
        this.seller.setFirstName(View.getStringInput("Enter your first name: ").trim());
    }

    private void editLastName() {
        this.seller.setLastName(View.getStringInput("Enter your last name: ").trim());
    }

    private void editStoreName() throws Exception {
        String storeName = View.getStringInput("Enter your store name: ").trim();
        if (storeName.equals(this.seller.getStoreName())) {
            throw new Exception(View.BRIGHT_YELLOW + "Current Store Name." + View.RESET);
        }
        this.pageManager.getUserManager().verifyStoreName(storeName);
        this.seller.setStoreName(storeName);
    }

    private void editStoreState() throws Exception {
        this.seller.setStoreState(View.getStringInput("Enter your store state: ").trim());
    }

    private void editPhone() throws Exception {
        String phone = View.getStringInput("Enter new phone number: ");
        this.pageManager.getUserManager().verifyPhone(phone);
        this.seller.setPhone(phone);
        pageManager.back();
    }
}
