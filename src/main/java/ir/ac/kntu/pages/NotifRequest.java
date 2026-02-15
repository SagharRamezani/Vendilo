package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.model.Customer;

public class NotifRequest extends Page {
    private PageManager pageManager;
    private Customer customer;

    public NotifRequest(PageManager pageManager, Customer customer) {
        super(PageTitle.NOTIF_REQUEST);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        pageManager.navigateToPage(PageTitle.CUSTOMER_REQUEST, customer);
    }
}
