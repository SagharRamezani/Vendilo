package ir.ac.kntu.model;

import ir.ac.kntu.View;

import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private String storeCode;
    private String phone;
    private String storeName;
    private String storeState;
    private String nationalId;
    private List<Product> products;
    private Wallet wallet;
    private List<Receipt> receipts;
    private boolean active;
    private boolean rejected;

    public Seller(String firstName, String lastName, String phone, String password, String storeCode, String storeName, String storeState, String nationalId) throws Exception {
        super(firstName, lastName, password);
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.storeState = storeState;
        this.nationalId = nationalId;
        this.products = new ArrayList<>();
        this.wallet = new Wallet();
        this.receipts = new ArrayList<>();
        setPhone(phone);
        this.active = false;
        this.rejected = false;
    }

    public Seller(String firstName, String lastName, String phone, String password, String storeName, String storeState, String nationalId) throws Exception {
        super(firstName, lastName, password);
        this.storeName = storeName;
        this.storeState = storeState;
        this.nationalId = nationalId;
        this.products = new ArrayList<>();
        this.wallet = new Wallet();
        this.receipts = new ArrayList<>();
        setPhone(phone);
        this.active = false;
        this.rejected = false;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void setPhone(String phone) throws Exception {
        if (phone.length() != 11 || phone.matches(".*[^0-9].*")) {
            throw new Exception(View.RED + "Invalid Phone Number" + View.RESET);
        }
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Role getAccessLevel() {
        return Role.Seller;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public String getId() {
        return nationalId;
    }

    public void setId(String nationalId) {
        this.nationalId = nationalId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seller s)) {
            return false;
        }
        return storeCode != null && storeCode.equals(s.storeCode);
    }

    @Override
    public int hashCode() {
        return storeCode == null ? 0 : storeCode.hashCode();
    }
}