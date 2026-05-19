package ir.ac.kntu.model;

import ir.ac.kntu.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private double balance;
    private List<Transaction> transactions;

    public Wallet() {
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public Wallet(double balance) {
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return this.balance;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public List<Transaction> afterTransactions(LocalDate date) {
        int index = 0;
        for (Transaction transaction : this.transactions) {
            if (transaction.getDate().isBefore(date)) {
                index++;
            } else {
                break;
            }
        }
        return new ArrayList<>(this.transactions.subList(index, this.transactions.size()));
    }

    public void deposit(double amount) {
        this.balance += amount;
        this.transactions.add(new Transaction(amount, TransactionType.DEPOSIT));
    }

    public void withdraw(double amount) throws Exception {
        if (amount > this.balance) {
            throw new Exception(View.RED + "Insufficient Balance" + View.RESET);
        }
        this.balance -= amount;
        this.transactions.add(new Transaction(amount, TransactionType.WITHDRAWAL));
    }
}
