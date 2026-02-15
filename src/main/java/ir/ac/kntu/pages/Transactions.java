package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Seller;
import ir.ac.kntu.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Transactions extends Page {
    private PageManager pageManager;
    private List<Transaction> transactions;

    public Transactions(PageManager pageManager, Customer customer) {
        super(PageTitle.TRANSACTIONS);
        this.pageManager = pageManager;
        this.transactions = customer.getWallet().getTransactions();
    }

    public Transactions(PageManager pageManager, Seller seller) {
        super(PageTitle.TRANSACTIONS);
        this.pageManager = pageManager;
        this.transactions = seller.getWallet().getTransactions();
    }

    @Override
    public void show() throws Exception {
        try {
            if (Boolean.parseBoolean(View.getStringInput(View.BRIGHT_WHITE + "Filter by date: (true or false) " + View.RESET))) {
                filterDate();
            } else {
                System.out.println(View.BRIGHT_CYAN + "&&& Transactions &&&" + View.RESET);
                View.extracted(this.transactions);
            }
            pageManager.back();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        pageManager.back();
    }

    public List<Transaction> filterTransactions(LocalDate start, LocalDate end) {
        return this.transactions.stream().filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end)).collect(Collectors.toList());
    }

    private void filterDate() throws Exception {
        LocalDate start = LocalDate.parse(View.getStringInput("Start date: (YYYY-MM-dd)"));
        LocalDate end = LocalDate.parse(View.getStringInput("End date: (YYYY-MM-dd)"));
        System.out.println(View.BRIGHT_CYAN + "&&& Transactions &&&" + View.RESET);
        View.extracted(filterTransactions(start, end));
    }
}