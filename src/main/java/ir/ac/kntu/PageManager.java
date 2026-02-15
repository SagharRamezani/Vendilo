package ir.ac.kntu;

import ir.ac.kntu.model.*;
import ir.ac.kntu.pages.*;
import ir.ac.kntu.util.ExitAppException;

import java.util.Stack;

public class PageManager {
    private Page currentPage;
    private UserManager userManager;
    private Stack<Page> pageStack;
    private ProductManager productManager;

    public PageManager(UserManager userManager) {
        this.userManager = userManager;
        this.pageStack = new Stack<>();
        this.productManager = new ProductManager();
    }

    public void start() throws Exception {
        setCurrentPage(new Welcome(this));
        while (true) {
            try {
                this.userManager.autoReplyOldTickets();
                this.currentPage.show();
            } catch (ExitAppException e) {
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                try {
                    View.getStringInput("Press Enter...");
                } catch (Exception ignore) { /* no-op */ }
                // fallback to welcome if stack is empty
                if (pageStack.isEmpty()) {
                    setCurrentPage(new Welcome(this));
                } else {
                    back();
                }
            }
        }
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void navigateToPage(PageTitle title) throws Exception {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        switch (title) {
            case AUTHENTICATION -> this.currentPage = new Authentication(this);
            case SEARCH -> this.currentPage = new Search(this, this.getProductManager());
            case WELCOME, LOGOUT -> welcome();
            case CUSTOMER_LOGIN -> this.currentPage = new CustomerLogin(this);
            case CUSTOMER_REGISTER -> this.currentPage = new CustomerRegister(this);
            case SELLER_REGISTER -> this.currentPage = new SellerRegister(this);
            case SELLER_LOGIN -> this.currentPage = new SellerLogin(this);
            case SUPPORTER_LOGIN -> this.currentPage = new SupporterLogin(this);
            case EXIT -> this.currentPage = new Exit();
            case STORE -> this.currentPage = new Store(this);
            case MANAGER_LOGIN -> this.currentPage = new ManagerLogin(this);
            default -> throw new Exception("Invalid page title");
        }
    }

    private void welcome() {
        this.pageStack.clear();
        this.currentPage = new Welcome(this);
    }

    public void navigateToPage(PageTitle title, Manager manager) throws Exception {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        switch (title) {
            case MANAGER_HOME -> this.currentPage = new ManagerHome(this, manager);
            case SUPPORTER_REQUEST -> this.currentPage = new SupporterRequest(this);
            case SUPPORTER_RECEIPT -> this.currentPage = new SupporterReceipt(this);
            case MANAGE_USERS -> this.currentPage = new ManageUsers(this, manager);
            default -> throw new Exception("Invalid page title");
        }
    }

    public void navigateToPage(PageTitle title, Supporter supporter) throws Exception {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        switch (title) {
            case SUPPORTER_HOME -> this.currentPage = new SupporterHome(this, supporter);
            case SUPPORTER_REQUEST -> this.currentPage = new SupporterRequest(this);
            case SUPPORTER_RECEIPT -> this.currentPage = new SupporterReceipt(this);
            default -> throw new Exception("Invalid page title");
        }
    }

    public void navigateToPage(PageTitle title, Seller seller) throws Exception {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        switch (title) {
            case SELLER_WALLET -> this.currentPage = new SellerWallet(this, seller);
            case SELLER_HOME -> this.currentPage = new SellerHome(this, seller);
            case SELLER_PRODUCT -> this.currentPage = new SellerProduct(this, seller);
            case RECEIPT -> this.currentPage = new Receipts(this, seller);
            case TRANSACTIONS -> this.currentPage = new Transactions(this, seller);
            case SELLER_NOT -> this.currentPage = new SellerNot(this, seller);
            case SELLER_INFO -> this.currentPage = new SellerInfo(this, seller);
            default -> throw new Exception("Invalid page title");
        }
    }

    public void navigateToPage(PageTitle title, Customer customer) throws Exception {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        switch (title) {
            case RECEIPT -> this.currentPage = new Receipts(this, customer);
            case PROFILE -> this.currentPage = new Profile(this, customer);
            case EDIT_PROFILE -> this.currentPage = new EditProfile(this, customer);
            case ADDRESSES -> this.currentPage = new Addresses(this, customer);
            case CUSTOMER_WALLET -> this.currentPage = new CustomerWallet(this, customer);
            case CUSTOMER_HOME -> this.currentPage = new CustomerHome(this, customer);
            case CART -> this.currentPage = new CustomerCart(this, customer);
            case STORE -> this.currentPage = new Store(this);
            case COMMENTS -> this.currentPage = new Comments(this, customer);
            case CUSTOMER_REQUEST -> this.currentPage = new CustomerRequest(this, customer);
            case TRANSACTIONS -> this.currentPage = new Transactions(this, customer);
            case NOTIFICATIONS -> this.currentPage = new Notifications(this, customer);
            case NOTIF_REQUEST -> this.currentPage = new NotifRequest(this, customer);
            case PLUS_MEMBERSHIP -> this.currentPage = new PlusMembership(this, customer);
            default -> throw new Exception("Invalid page title");
        }
    }

    public void back() throws Exception {
        if (pageStack.isEmpty()) {
            throw new Exception("No page to back");
        }
        this.currentPage = pageStack.pop();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void navigateToPage(PageTitle pageTitle, String body) throws Exception {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        Product product = productManager.getProductByName(body);
        this.currentPage = new ProductInfo(this, product);
    }

    public void navigateToPage(PageTitle pageTitle, Product product) {
        if (this.currentPage != null) {
            this.pageStack.push(this.currentPage);
        }
        this.currentPage = new ProductInfo(this, product);
    }
}