package ir.ac.kntu.model;

import ir.ac.kntu.Verify;
import ir.ac.kntu.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Customer extends User {
    private String email;
    private String phone;
    private Wallet wallet;
    private List<Address> addresses;
    private Cart cart;
    private List<Receipt> receipts;
    private PlusSubscription plusSubscription;
    private List<Notification> notifications;
    private List<SupportTicket> requests;
    private List<Comment> comments;

    public Customer(String firstName, String lastName, String email, String phone, String password, double balance) throws Exception {
        super(firstName, lastName, password);
        this.wallet = new Wallet(balance);
        this.addresses = new ArrayList<>();
        this.cart = new Cart();
        setEmail(email);
        setPhone(phone);
        this.receipts = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.plusSubscription = new PlusSubscription(0);
    }

    public Customer(String firstName, String lastName, String email, String phone, String password) throws Exception {
        super(firstName, lastName, password);
        this.wallet = new Wallet();
        this.addresses = new ArrayList<>();
        this.cart = new Cart();
        setEmail(email);
        setPhone(phone);
        this.receipts = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.plusSubscription = new PlusSubscription(0);
    }

    public int unreadNotifications() {
        int count = 0;
        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                count++;
            }
        }
        return count;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void request(SupportTicket request) {
        this.requests.add(request);
    }

    public List<SupportTicket> getRequests() {
        return requests;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public Address getAddress(String title) throws Exception {
        for (Address address : this.addresses) {
            if (address.getTitle().equals(title)) {
                return address;
            }
        }
        throw new Exception(View.BRIGHT_YELLOW + "Address not found" + View.RESET);
    }

    public void buy(Address address) throws Exception {
        boolean stateChange = false;
        for (Product product : this.cart.getCart().keySet()) {
            if (!address.getState().equals(this.cart.getCart().get(product).getStoreState())) {
                stateChange = true;
            }
        }
        if (!stateChange && plusSubscription.isActive()) {
            this.cart.setShippingPrice(0);
        } else {
            this.cart.setShippingPrice(15000);
        }
        if (this.wallet.getBalance() < this.cart.getTotalPrice()) {
            throw new Exception(View.RED + "Not enough money!" + View.RESET);
        }
        this.wallet.withdraw(this.cart.getTotalPrice());
        this.cart.getDiscountCode().consumeOnceIfApplied();
        for (Product product : this.cart.getCart().keySet()) {
            this.cart.getCart().get(product).getWallet().deposit(product.getPrice() * 0.9);
        }
        receipts.add(new Receipt(this.cart, this, address));
        this.cart = new Cart();
    }

    public void setEmail(String str) throws Exception {
        str = str.toLowerCase();
        Verify.verifyEmail(str);
        this.email = str;
    }

    public void setPhone(String phone) throws Exception {
        Verify.verifyPhone(phone);
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Cart getCart() {
        return cart;
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
    }

    public void updateAddress(Address oldAddress, Address newAddress) {
        this.addresses.set(addresses.indexOf(oldAddress), newAddress);
    }

    public boolean isPlus() {
        return this.plusSubscription.isActive();
    }

    public void activatePlus(long days) {
        this.plusSubscription = new PlusSubscription(days);
    }

    public boolean isPlusActive() {
        return this.plusSubscription != null && this.plusSubscription.isActive();
    }

    public PlusSubscription getPlus() {
        return this.plusSubscription;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification n) {
        this.notifications.add(n);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer c)) {
            return false;
        }
        return email != null && email.equalsIgnoreCase(c.email);
    }

    @Override
    public int hashCode() {
        return email == null ? 0 : email.toLowerCase(Locale.ROOT).hashCode();
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Role getAccessLevel() {
        return Role.Customer;
    }

    public PlusSubscription getPlusSubscription() {
        return this.plusSubscription;
    }

    public void setPlusSubscription(PlusSubscription plusSubscription) {
        this.plusSubscription = plusSubscription;
    }
}