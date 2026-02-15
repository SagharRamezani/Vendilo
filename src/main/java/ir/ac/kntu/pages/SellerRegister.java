package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;

public class SellerRegister extends Page {
    private PageManager pageManager;

    public SellerRegister(PageManager pageManager) {
        super(PageTitle.SELLER_REGISTER);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Seller Register &&&" + View.RESET);
            String firstName = View.getStringInput("Enter your first name: ").trim();
            String lastName = View.getStringInput("Enter your last name: ").trim();
            String storeName = View.getStringInput("Enter your store name: ").trim();
            String storeState = View.getStringInput("Enter your store state: ").trim();
            String phoneNumber = View.getStringInput("Enter your phone number: ").trim();
            String nationalId = View.getStringInput("Enter your national id: ").trim();
            String password = View.getStringInput("Enter your password: ").trim();
            Seller seller = new Seller(firstName, lastName, phoneNumber, password, storeName, storeState, nationalId);
            pageManager.getUserManager().unknownSeller(seller);
            System.out.println(View.BRIGHT_GREEN + "Your request is sent." + View.BRIGHT_WHITE + "Store Code: " + seller.getStoreCode() + View.RESET);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        pageManager.back();
    }
}