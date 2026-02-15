package ir.ac.kntu;

import ir.ac.kntu.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private List<Customer> customers;
    private List<Seller> sellers;
    private List<Supporter> supporters;
    private List<Manager> managers;
    private List<DiscountCode> globalDiscounts;
    private Map<String, String> rejectedSellers;
    private List<SupportTicket> supportTickets;

    public UserManager() {
        this.customers = new ArrayList<>();
        this.sellers = new ArrayList<>();
        this.supporters = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.globalDiscounts = new ArrayList<>();
        this.rejectedSellers = new HashMap<>();
        this.supportTickets = new ArrayList<>();
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public Manager authManager(String username, String password) throws Exception {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return manager;
            }
        }
        throw new Exception(View.BRIGHT_YELLOW + "User not found" + View.RESET);
    }

    public List<Supporter> getSupporters() {
        return supporters;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public List<DiscountCode> getGlobalDiscounts() {
        return globalDiscounts;
    }

    public void addGlobalDiscount(DiscountCode discount) {
        globalDiscounts.add(discount);
    }

    public List<Seller> getUnSellers() {
        List<Seller> unSellers = new ArrayList<>();
        for (Seller seller : sellers) {
            if (!seller.isActive() && !seller.isRejected()) {
                unSellers.add(seller);
            }
        }
        return unSellers;
    }

    public void verifySeller(Seller seller) throws Exception {
        for (Seller s : this.sellers) {
            if (s.getPhone().equals(seller.getPhone())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
            }
            if (s.getId().equals(seller.getId())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Id already exists" + View.RESET);
            }
            if (s.getStoreName().equals(seller.getStoreName())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Store Name already exists" + View.RESET);
            }
        }
        for (Customer s : this.customers) {
            if (s.getPhone().equals(seller.getPhone())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
            }
        }
    }

    public void request(Customer customer, RequestType subject, String msg) {
        SupportTicket request = new SupportTicket(subject, msg, customer);
        supportTickets.add(request);
        customer.request(request);
    }

    public List<SupportTicket> getTickets() {
        return this.supportTickets;
    }

    public void respondToTicket(int index, String message) throws Exception {
        if (index >= 0 && index < supportTickets.size()) {
            supportTickets.get(index).respond(message);
        } else {
            throw new Exception(View.BRIGHT_RED + "Out of range." + View.RESET);
        }
    }

    public void verifyPhone(String phone) throws Exception {
        Verify.verifyPhone(phone);
        for (Customer c : this.customers) {
            if (c.getPhone().equals(phone)) {
                throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
            }
        }
        for (Seller c : this.sellers) {
            if (c.getPhone().equals(phone)) {
                throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
            }
        }
    }

    public void verifyEmail(String email) throws Exception {
        Verify.verifyEmail(email);
        for (Customer c : this.customers) {
            if (c.getEmail().equals(email)) {
                throw new Exception(View.BRIGHT_YELLOW + "This Email already exists" + View.RESET);
            }
        }
    }

    public void rejectSeller(Seller seller, String message) {
        seller.setRejected(true);
        this.rejectedSellers.put(seller.getStoreCode(), message);
    }

    public void rejectSeller(String storeCode, String message) throws Exception {
        Seller seller = getSellerByCode(storeCode);
        seller.setRejected(true);
        this.rejectedSellers.put(seller.getStoreCode(), message);
    }

    public Seller logInSeller(String store, String password) throws Exception {
        for (Seller seller : sellers) {
            if (seller.getStoreCode().equals(store) && seller.getPassword().equals(password)) {
                return seller;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Seller not found" + View.RESET);
    }

    public void verifyStoreName(String storeName) throws Exception {
        for (Seller seller : sellers) {
            if (seller.getStoreName().equalsIgnoreCase(storeName.trim())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Store Name already exists" + View.RESET);
            }
        }
    }

    public String isRejected(Seller seller) {
        if (seller == null || seller.getStoreCode() == null) {
            return null;
        }
        if (this.rejectedSellers.containsKey(seller.getStoreCode())) {
            return rejectedSellers.get(seller.getStoreCode());
        }
        return null;
    }

    public void verifyCustomer(Customer customer) throws Exception {
        for (Customer c : this.customers) {
            if (c.getEmail().equals(customer.getEmail())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Email already exists" + View.RESET);
            }
            if (c.getPhone().equals(customer.getPhone())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
            }
        }
    }

    public Supporter logInSupporter(String username, String password) throws Exception {
        for (Supporter supporter : this.supporters) {
            if (supporter.getPassword().equals(password) && supporter.getUserName().equals(username)) {
                return supporter;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Supporter not found" + View.RESET);
    }

    public Customer logInCustomerE(String email, String password) throws Exception {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    public Customer logInCustomerP(String phone, String password) throws Exception {
        for (Customer customer : customers) {
            if (customer.getPhone().equals(phone) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    public void registerCustomer(Customer customer) throws Exception {
        this.verifyCustomer(customer);
        customers.add(customer);
    }

    public int sellerCount() {
        return sellers.size();
    }

    public void unknownSeller(Seller seller) throws Exception {
        this.verifySeller(seller);
        String storeCode = "" + seller.getFirstName().charAt(0) + seller.getLastName().charAt(0) + (7000 + this.sellerCount());
        seller.setStoreCode(storeCode);
        sellers.add(seller);
    }

    public Seller getSeller(String storeName) throws Exception {
        for (Seller seller : sellers) {
            if (seller.getStoreName().equals(storeName)) {
                return seller;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Seller not found" + View.RESET);
    }

    public Seller getSellerByCode(String storeCode) throws Exception {
        for (Seller seller : sellers) {
            if (seller.getStoreCode().equals(storeCode)) {
                return seller;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Seller not found" + View.RESET);
    }

    public void registerSeller(String store) throws Exception {
        Seller seller = getSellerByCode(store);
        seller.activate();
    }

    public void registerSeller(Seller seller) throws Exception {
        String storeCode = "" + seller.getFirstName().charAt(0) + seller.getLastName().charAt(0) + String.valueOf(7000 + this.sellerCount());
        seller.setStoreCode(storeCode);
        seller.activate();
        sellers.add(seller);
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public void addSeller(Seller seller) {
        this.sellers.add(seller);
    }

    public void addSupporter(Supporter supporter) {
        this.supporters.add(supporter);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public Customer getCustomerE(String email) throws Exception {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    public Customer getCustomerP(String phone) throws Exception {
        for (Customer customer : customers) {
            if (customer.getPhone().equals(phone)) {
                return customer;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    // auto-reply if pending > 24h
    public void autoReplyOldTickets() {
        java.time.Instant now = ir.ac.kntu.util.Calendar.now();
        for (SupportTicket t : this.supportTickets) {
            if ((t.getResponse() == null || t.getResponse().isEmpty())
                    && "pending".equalsIgnoreCase(t.getStatus())) {
                long hours = java.time.Duration.between(t.getCreatedAt(), now).toHours();
                if (hours >= 24) {
                    t.setResponse("Our colleagues will contact you soon.");
                    t.setStatus("auto-replied");
                }
            }
        }
    }

    public void addUser(User u) throws Exception {
        if (u instanceof Customer c) {
            this.customers.add(c);
        } else if (u instanceof Seller s) {
            this.sellers.add(s);
        } else if (u instanceof Supporter s) {
            this.supporters.add(s);
        } else if (u instanceof Manager manager) {
            this.managers.add(manager);
        } else {
            throw new Exception(View.BRIGHT_RED + "Unknown User" + View.RESET);
        }
    }
}
