package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Customer;

public class CustomerWallet extends Page {
    private PageManager pageManager;
    private Customer customer;

    public CustomerWallet(PageManager pageManager, Customer customer) {
        super(PageTitle.CUSTOMER_WALLET);
        this.pageManager = pageManager;
        this.customer = customer;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Wallet &&&" + View.RESET);
            String choice = View.getStringInput(View.BRIGHT_BLUE + "1.View Balance\n2.Deposit\n3.Withdraw\n4.Transactions\n5.BACK\n6.EXIT" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            switch (choice) {
                case "1" -> viewBalance();
                case "2" -> deposit();
                case "3" -> withdraw();
                case "4" -> this.pageManager.navigateToPage(PageTitle.TRANSACTIONS, this.customer);
                case "5" -> this.pageManager.back();
                case "6" -> this.pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    private void withdraw() throws Exception {
        this.customer.getWallet().withdraw(Integer.parseInt(View.getStringInput("Enter amount to withdraw: ")));
        System.out.println(View.BRIGHT_GREEN + "withdraw successful" + View.RESET);
        pageManager.back();
    }

    private void deposit() throws Exception {
        this.customer.getWallet().deposit(Integer.parseInt(View.getStringInput("Enter amount to deposit: ")));
        System.out.println(View.BRIGHT_GREEN + "deposit successful" + View.RESET);
        pageManager.back();
    }

    private void viewBalance() throws Exception {
        System.out.println(View.BRIGHT_BLUE + "Your Balance: " + this.customer.getWallet().getBalance() + View.RESET);
        pageManager.back();
    }
}