package ir.ac.kntu.pages;

import java.util.Scanner;

public abstract class Page {
    private PageTitle title;
    private Scanner scanner;

    public Page(PageTitle title) {
        this.title = title;
        this.scanner = new Scanner(System.in);
    }

    public PageTitle getTitle() {
        return this.title;
    }

    public Scanner scanner() {
        return this.scanner;
    }

    public abstract void show() throws Exception;
}
