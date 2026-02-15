package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;

public class SellerHome extends Page {
    private PageManager pageManager;
    private Seller seller;

    public SellerHome(PageManager pageManager, Seller seller) {
        super(PageTitle.SELLER_HOME);
        this.pageManager = pageManager;
        this.seller = seller;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Seller Home &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Wallet\n2.Products\n3.Receipts\n4.Logout\n5.BACK\n6.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> pageManager.navigateToPage(PageTitle.SELLER_WALLET, this.seller);
                case "2" -> pageManager.navigateToPage(PageTitle.SELLER_PRODUCT, this.seller);
                case "3" -> pageManager.navigateToPage(PageTitle.RECEIPT, this.seller);
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
}