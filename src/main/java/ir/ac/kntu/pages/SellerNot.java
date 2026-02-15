package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;

public class SellerNot extends Page {
    private PageManager pageManager;
    private Seller seller;

    public SellerNot(PageManager pageManager, Seller seller) {
        super(PageTitle.SELLER_NOT);
        this.pageManager = pageManager;
        this.seller = seller;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_YELLOW + "&&& Not Active &&&" + View.RESET);
            if(rejectedProcess()){
                rejectedPage();
            } else {
                String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Edit info\n2.Logout\n3.BACK\n4.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
                switch (choice) {
                    case "1" -> pageManager.navigateToPage(PageTitle.SELLER_INFO, this.seller);
                    case "2" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                    case "3" -> pageManager.back();
                    case "4" -> pageManager.navigateToPage(PageTitle.EXIT);
                    default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void rejectedPage() throws Exception {
        String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Edit info\n2.Submit new request\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
        switch (choice) {
            case "1" -> pageManager.navigateToPage(PageTitle.SELLER_INFO, this.seller);
            case "2" -> submit();
            case "3" -> pageManager.navigateToPage(PageTitle.LOGOUT);
            case "4" -> pageManager.back();
            case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
            default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
        }
    }

    private void submit() throws Exception {
        seller.setRejected(false);
        System.out.println(View.BRIGHT_GREEN + "Your request is sent." + View.RESET);
    }

    private boolean rejectedProcess() {
        String msg = this.pageManager.getUserManager().isRejected(this.seller);
        if (msg != null) {
            System.out.println(View.BRIGHT_RED + "Your request is REJECTED: " + View.RESET + msg);
        } else {
            System.out.println(View.BRIGHT_YELLOW + "Your request has not been approved yet" + View.RESET);
        }
        return (msg != null);
    }
}
