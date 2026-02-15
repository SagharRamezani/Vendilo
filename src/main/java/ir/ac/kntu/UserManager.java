package ir.ac.kntu;

import ir.ac.kntu.model.*;
import ir.ac.kntu.util.PasswordHasher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private List<Customer> customers;
    private List<Seller> sellers;
    private List<Supporter> supporters;
    private List<Manager> managers;

    // Fast indices (keep lists too for order/iteration).
    private final Map<String, Customer> customersByEmail = new HashMap<>();
    private final Map<String, Customer> customersByPhone = new HashMap<>();
    private final Map<String, Seller> sellersByStoreCode = new HashMap<>();
    private final Map<String, Seller> sellersByPhone = new HashMap<>();
    private final Map<String, Seller> sellersByStoreNameLower = new HashMap<>();
    private final Map<String, Supporter> supportersByUsernameLower = new HashMap<>();
    private final Map<String, Manager> managersByUsernameLower = new HashMap<>();
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
        if (manager.getUsername() != null) {
            managersByUsernameLower.put(manager.getUsername().toLowerCase(), manager);
        }
    }

    public Manager authManager(String username, String password) throws Exception {
        Manager manager = managersByUsernameLower.get(username.toLowerCase());
        if (manager != null && PasswordHasher.matches(manager.getPassword(), password)) {
            return manager;
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
        if (seller == null) {
            throw new Exception(View.BRIGHT_YELLOW + "Invalid seller" + View.RESET);
        }
        if (seller.getPhone() != null && (sellersByPhone.containsKey(seller.getPhone())
                || customersByPhone.containsKey(seller.getPhone()))) {
            throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
        }
        if (seller.getStoreName() != null
                && sellersByStoreNameLower.containsKey(seller.getStoreName().trim().toLowerCase())) {
            throw new Exception(View.BRIGHT_YELLOW + "This Store Name already exists" + View.RESET);
        }
        // ID uniqueness is still list-based because it may be null and rarely queried.
        for (Seller s : this.sellers) {
            if (s.getId() != null && s.getId().equals(seller.getId())) {
                throw new Exception(View.BRIGHT_YELLOW + "This Id already exists" + View.RESET);
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
        if (customersByPhone.containsKey(phone) || sellersByPhone.containsKey(phone)) {
            throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
        }
    }

    public void verifyEmail(String email) throws Exception {
        Verify.verifyEmail(email);
        if (email != null && customersByEmail.containsKey(email.trim().toLowerCase())) {
            throw new Exception(View.BRIGHT_YELLOW + "This Email already exists" + View.RESET);
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
        Seller seller = sellersByStoreCode.get(store);
        if (seller != null && PasswordHasher.matches(seller.getPassword(), password)) {
            return seller;
        }
        throw new Exception(View.BRIGHT_RED + "Seller not found" + View.RESET);
    }

    public void verifyStoreName(String storeName) throws Exception {
        if (storeName == null) {
            throw new Exception(View.BRIGHT_YELLOW + "Invalid store name" + View.RESET);
        }
        if (sellersByStoreNameLower.containsKey(storeName.trim().toLowerCase())) {
            throw new Exception(View.BRIGHT_YELLOW + "This Store Name already exists" + View.RESET);
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
        if (customer.getEmail() != null && customersByEmail.containsKey(customer.getEmail().trim().toLowerCase())) {
            throw new Exception(View.BRIGHT_YELLOW + "This Email already exists" + View.RESET);
        }
        if (customer.getPhone() != null && customersByPhone.containsKey(customer.getPhone())) {
            throw new Exception(View.BRIGHT_YELLOW + "This Phone already exists" + View.RESET);
        }
    }

    public Supporter logInSupporter(String username, String password) throws Exception {
        Supporter supporter = supportersByUsernameLower.get(username.toLowerCase());
        if (supporter != null && PasswordHasher.matches(supporter.getPassword(), password)) {
            return supporter;
        }
        throw new Exception(View.BRIGHT_RED + "Supporter not found" + View.RESET);
    }

    public Customer logInCustomerE(String email, String password) throws Exception {
        Customer customer = customersByEmail.get(email.trim().toLowerCase());
        if (customer != null && PasswordHasher.matches(customer.getPassword(), password)) {
            return customer;
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    public Customer logInCustomerP(String phone, String password) throws Exception {
        Customer customer = customersByPhone.get(phone);
        if (customer != null && PasswordHasher.matches(customer.getPassword(), password)) {
            return customer;
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    public void registerCustomer(Customer customer) throws Exception {
        this.verifyCustomer(customer);
        customers.add(customer);
        indexCustomer(customer);
    }

    public int sellerCount() {
        return sellers.size();
    }

    public void unknownSeller(Seller seller) throws Exception {
        this.verifySeller(seller);
        String storeCode = "" + seller.getFirstName().charAt(0) + seller.getLastName().charAt(0) + (7000 + this.sellerCount());
        seller.setStoreCode(storeCode);
        sellers.add(seller);
        indexSeller(seller);
    }

    public Seller getSeller(String storeName) throws Exception {
        if (storeName != null) {
            Seller seller = sellersByStoreNameLower.get(storeName.trim().toLowerCase());
            if (seller != null) {
                return seller;
            }
        }
        throw new Exception(View.BRIGHT_RED + "Seller not found" + View.RESET);
    }

    public Seller getSellerByCode(String storeCode) throws Exception {
        Seller seller = sellersByStoreCode.get(storeCode);
        if (seller != null) {
            return seller;
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
        indexSeller(seller);
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
        indexCustomer(customer);
    }

    public void addSeller(Seller seller) {
        this.sellers.add(seller);
        indexSeller(seller);
    }

    public void addSupporter(Supporter supporter) {
        this.supporters.add(supporter);
        if (supporter.getUserName() != null) {
            supportersByUsernameLower.put(supporter.getUserName().toLowerCase(), supporter);
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public Customer getCustomerE(String email) throws Exception {
        Customer customer = customersByEmail.get(email.trim().toLowerCase());
        if (customer != null) {
            return customer;
        }
        throw new Exception(View.BRIGHT_RED + "Customer not found" + View.RESET);
    }

    public Customer getCustomerP(String phone) throws Exception {
        Customer customer = customersByPhone.get(phone);
        if (customer != null) {
            return customer;
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
            indexCustomer(c);
        } else if (u instanceof Seller s) {
            this.sellers.add(s);
            indexSeller(s);
        } else if (u instanceof Supporter s) {
            this.supporters.add(s);
            if (s.getUserName() != null) {
                supportersByUsernameLower.put(s.getUserName().toLowerCase(), s);
            }
        } else if (u instanceof Manager manager) {
            this.managers.add(manager);
            if (manager.getUsername() != null) {
                managersByUsernameLower.put(manager.getUsername().toLowerCase(), manager);
            }
        } else {
            throw new Exception(View.BRIGHT_RED + "Unknown User" + View.RESET);
        }
    }

    private void indexCustomer(Customer c) {
        if (c == null) {
            return;
        }
        if (c.getEmail() != null) {
            customersByEmail.put(c.getEmail().trim().toLowerCase(), c);
        }
        if (c.getPhone() != null) {
            customersByPhone.put(c.getPhone(), c);
        }
    }

    private void indexSeller(Seller s) {
        if (s == null) {
            return;
        }
        if (s.getStoreCode() != null) {
            sellersByStoreCode.put(s.getStoreCode(), s);
        }
        if (s.getPhone() != null) {
            sellersByPhone.put(s.getPhone(), s);
        }
        if (s.getStoreName() != null) {
            sellersByStoreNameLower.put(s.getStoreName().trim().toLowerCase(), s);
        }
    }
}
