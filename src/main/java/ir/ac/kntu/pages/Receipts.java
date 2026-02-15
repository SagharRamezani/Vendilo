package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Receipt;
import ir.ac.kntu.model.Seller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Receipts extends Page {
    private PageManager pageManager;
    private List<Receipt> receipts;

    public Receipts(PageManager pageManager, Seller seller) {
        super(PageTitle.RECEIPT);
        this.pageManager = pageManager;
        this.receipts = seller.getReceipts();
    }

    public Receipts(PageManager pageManager, Customer customer) {
        super(PageTitle.RECEIPT);
        this.pageManager = pageManager;
        this.receipts = customer.getReceipts();
    }

    @Override
    public void show() throws Exception {
        try {
            if (Boolean.parseBoolean(View.getStringInput(View.BRIGHT_WHITE + "Filter by date: (true or false) " + View.RESET))) {
                filterDate();
            } else {
                System.out.println(View.BRIGHT_CYAN + "&&& Receipts &&&" + View.RESET);
                View.extracted(this.receipts);
            }
            pageManager.back();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        pageManager.back();
    }

    public List<Receipt> filterReceipts(LocalDate start, LocalDate end) {
        return this.receipts.stream().filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end)).collect(Collectors.toList());
    }

    private void filterDate() throws Exception {
        LocalDate start = LocalDate.parse(View.getStringInput("Start date: (YYYY-MM-dd)"));
        LocalDate end = LocalDate.parse(View.getStringInput("End date: (YYYY-MM-dd)"));
        System.out.println(View.BRIGHT_CYAN + "&&& Receipts &&&" + View.RESET);
        View.extracted(filterReceipts(start, end));
    }
}