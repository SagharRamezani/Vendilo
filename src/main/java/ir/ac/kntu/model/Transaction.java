package ir.ac.kntu.model;

import ir.ac.kntu.View;
import ir.ac.kntu.util.Calendar;

import java.time.LocalDate;
import java.time.ZoneId;

public class Transaction {
    private final double amount;
    private final TransactionType type;
    private final LocalDate date;

    public Transaction(double amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
        this.date = Calendar.now().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public double getAmount() {
        return this.amount;
    }

    public TransactionType getType() {
        return this.type;
    }

    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        String minimized = View.BRIGHT_BLUE + this.type.toString() + "\n";
        minimized += this.amount + "\n" + this.date + "\n" + View.RESET;
        return minimized;
    }
}
