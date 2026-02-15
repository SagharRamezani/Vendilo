package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;

import java.util.Random;

public class Authentication extends Page {
    private final PageManager pm;
    private String code;

    public Authentication(PageManager pm) {
        super(PageTitle.AUTHENTICATION);
        this.pm = pm;
    }

    @Override
    public void show() throws Exception {
        // simple 2FA (demo)
        code = gen(); // generate
        View.printLine("Auth code: " + code + View.BRIGHT_BLACK + " (demo)" + View.RESET);
        String in = View.getStringInput("Enter code: ").trim(); // read
        if (in.equals(code)) {
            View.printLine(View.BRIGHT_GREEN + "Authenticated" + View.RESET);
            pm.back(); // go back
        } else {
            throw new Exception(View.BRIGHT_RED + "Wrong code" + View.RESET);
        }
    }

    private String gen() {
        // 6-digit code
        Random r = new Random();
        int v = 100000 + r.nextInt(900000);
        return String.valueOf(v);
    }
}