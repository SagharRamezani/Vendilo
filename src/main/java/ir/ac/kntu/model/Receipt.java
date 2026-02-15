package ir.ac.kntu.model;

import ir.ac.kntu.View;
import ir.ac.kntu.util.Calendar;

import java.time.LocalDate;
import java.time.ZoneId;

public class Receipt {
    private Cart cart;
    private LocalDate date;
    private Customer customer;
    private Address address;

    public Receipt(Cart cart, Customer customer, Address address) {
        this.cart = cart;
        this.date = Calendar.now().atZone(ZoneId.systemDefault()).toLocalDate();
        this.customer = customer;
        this.address = address;
    }

    public LocalDate getDate() {
        return date;
    }

    public String minimizeView() {
        String minimized = View.BRIGHT_BLUE + this.cart.minimize() + "\n";
        minimized += this.cart.getTotalPrice() + "\n" + this.date + "\n" + View.RESET;
        return minimized;
    }

    @Override
    public String toString() {
        return minimizeView();
    }

    public String fullView() {
        String full = View.BRIGHT_MAGENTA + this.date + "\n";
        full += this.cart.full() + "\n";
        full += this.customer.getEmail() + "\n";
        full += this.address.toString() + "\n" + View.RESET;
        return full;
    }
}
