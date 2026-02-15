package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Notification;

import java.util.List;

public class Notifications extends Page {
    private final PageManager pageManager;
    private final Customer customer;

    public Notifications(PageManager pm, Customer customer) {
        super(PageTitle.NOTIFICATIONS);
        this.pageManager = pm;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Notifications &&&" + View.RESET);
            List<Notification> list = customer.getNotifications();
            if (list.isEmpty()) {
                System.out.println(View.BRIGHT_BLACK + "No notifications." + View.RESET);
                pageManager.back();
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                Notification notif = list.get(i);
                String mark = notif.isRead() ? "" : View.BRIGHT_BLUE + "[NEW] " + View.RESET;
                System.out.println((i + 1) + ") " + mark + notif);
            }
            int idx = View.getInt("Open index: (0=back) ");
            if (idx == 0) {
                pageManager.back();
                return;
            }
            if (idx < 1 || idx > list.size()) {
                throw new Exception("Out of range");
            }
            Notification notif = list.get(idx - 1);
            notif.markRead();
            System.out.println(View.BRIGHT_WHITE + notif.view() + View.RESET);
            System.out.println(notif.getBody());

            switch (notif.getType()) {
                case STOCK:
                    pageManager.navigateToPage(PageTitle.PRODUCT, notif.getBody());
                    break;
                case TICKET:
                    pageManager.navigateToPage(PageTitle.NOTIF_REQUEST, customer);
                    break;
                case DISCOUNT, BROADCAST:
                    System.out.println(View.BRIGHT_WHITE + notif.view() + View.RESET);
                    pageManager.back();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}
