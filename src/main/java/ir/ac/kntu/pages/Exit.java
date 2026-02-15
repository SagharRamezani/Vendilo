package ir.ac.kntu.pages;

public class Exit extends Page {

    public Exit() {
        super(PageTitle.EXIT);
    }

    @Override
    public void show() throws Exception {
        System.out.println("Closing the application ...");
        throw new Exception("EXIT");
    }
}
