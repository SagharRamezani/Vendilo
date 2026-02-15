package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Seller;
import ir.ac.kntu.model.SupportTicket;

import java.util.List;
import java.util.stream.Collectors;

public class SupporterRequest extends Page {
    private PageManager pageManager;
    private List<Seller> unSellers;
    private List<SupportTicket> Tickets;

    public SupporterRequest(PageManager pageManager) {
        super(PageTitle.SUPPORTER_REQUEST);
        this.pageManager = pageManager;
        this.unSellers = pageManager.getUserManager().getUnSellers();
        this.Tickets = pageManager.getUserManager().getTickets();
    }

    public List<SupportTicket> noRespondTickets() throws Exception {
        return Tickets.stream().filter(ticket -> "pending".equals(ticket.getStatus())).collect(Collectors.toList());
    }

    private void viewNoResponds() throws Exception {
        List<SupportTicket> noRespondTickets = noRespondTickets();
        View.extracted(noRespondTickets);
    }

    public void authentication() throws Exception {
        String store = View.getStringInput("Enter Store Code: ");
        String choice = View.getStringInput("1.Accept\n2.reject\nChoose an option: ");
        switch (choice) {
            case "1":
                this.pageManager.getUserManager().registerSeller(store);
                break;
            case "2":
                String msg = View.getStringInput("Enter Reason: ");
                this.pageManager.getUserManager().rejectSeller(store, msg);
                break;
            default:
                throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
        }
        pageManager.back();
    }

    public void requests() throws Exception {
        String choice = View.getStringInput(View.BRIGHT_WHITE + "1.UnRegistered Sellers\n2.User Requests\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
        switch (choice) {
            case "1" -> View.extracted(this.unSellers);
            case "2" -> viewNoResponds();
            default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
        }
        pageManager.back();
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Requests &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_WHITE + "1.Authentication\n2.Requests\n3.Logout\n4.BACK\n5.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> authentication();
                case "2" -> requests();
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