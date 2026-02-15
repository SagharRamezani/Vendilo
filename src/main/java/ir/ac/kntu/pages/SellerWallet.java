package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;

public class SellerWallet extends Page {
    private PageManager pageManager;
    private Seller seller;

    public SellerWallet(PageManager pageManager, Seller seller) {
        super(PageTitle.SELLER_WALLET);
        this.pageManager = pageManager;
        this.seller = seller;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Wallet &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_BLUE + "1.View Balance\n2.Withdraw\n3.Transactions\n4.BACK\n5.EXIT" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewBalance();
                case "2" -> withdraw();
                case "3" -> pageManager.navigateToPage(PageTitle.TRANSACTIONS, this.seller);
                case "4" -> pageManager.back();
                case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void withdraw() throws Exception {
        String amountW = View.getStringInput("Enter amount to withdraw: ");
        this.seller.getWallet().withdraw(Integer.parseInt(amountW));
        System.out.println(View.BRIGHT_GREEN + "Withdraw successful" + View.RESET);
        pageManager.back();
    }

    private void viewBalance() throws Exception {
        System.out.println(View.BRIGHT_BLUE + "Your Balance: " + this.seller.getWallet().getBalance() + View.RESET);
        pageManager.back();
    }
}