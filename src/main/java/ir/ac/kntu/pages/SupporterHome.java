package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Supporter;

public class SupporterHome extends Page {
    private PageManager pageManager;
    private Supporter supporter;

    public SupporterHome(PageManager pageManager, Supporter supporter) {
        super(PageTitle.SUPPORTER_HOME);
        this.pageManager = pageManager;
        this.supporter = supporter;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Supporter Home &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Requests\n2.Receipts\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> pageManager.navigateToPage(PageTitle.SUPPORTER_REQUEST, this.supporter);
                case "2" -> pageManager.navigateToPage(PageTitle.SUPPORTER_RECEIPT, this.supporter);
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
}