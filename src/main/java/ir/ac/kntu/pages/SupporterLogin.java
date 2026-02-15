package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Supporter;

public class SupporterLogin extends Page {
    private PageManager pageManager;

    public SupporterLogin(PageManager pageManager) {
        super(PageTitle.SUPPORTER_LOGIN);
        this.pageManager = pageManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Supporter Login &&&" + View.RESET);
            String username = View.getStringInput("Enter username: ").trim();
            String pass = View.getStringInput("Enter password: ").trim();
            Supporter supporter = pageManager.getUserManager().logInSupporter(username, pass);
            System.out.println(View.BRIGHT_GREEN + "Wellcom, " + supporter.getFirstName() + View.RESET);
            pageManager.navigateToPage(PageTitle.SUPPORTER_HOME, supporter);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}