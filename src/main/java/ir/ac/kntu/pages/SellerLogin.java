package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;

public class SellerLogin extends Page {
    private PageManager pageManager;

    public SellerLogin(PageManager pageManager) {
        super(PageTitle.SELLER_LOGIN);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Seller Login &&&" + View.RESET);
            String storeCode = View.getStringInput("Enter store code: ").trim();
            String pass = View.getStringInput("Enter password: ").trim();
            Seller seller = pageManager.getUserManager().logInSeller(storeCode, pass);
            System.out.println(View.BRIGHT_GREEN + "Welcome, " + seller.getFirstName() + View.RESET);
            if (seller.isActive()) {
                pageManager.navigateToPage(PageTitle.SELLER_HOME, seller);
            } else {
                pageManager.navigateToPage(PageTitle.SELLER_NOT, seller);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}