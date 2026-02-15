package ir.ac.kntu.pages;

import ir.ac.kntu.util.ExitAppException;

public class Exit extends Page {

    public Exit() {
        super(PageTitle.EXIT);
    }

    @Override
    public void show() throws Exception {
        System.out.println("Closing the application ...");
        throw new ExitAppException();
    }
}
