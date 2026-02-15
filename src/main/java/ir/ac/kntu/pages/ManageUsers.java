package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.Verify;
import ir.ac.kntu.View;
import ir.ac.kntu.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageUsers extends Page {
    private Manager manager;
    private PageManager pageManager;

    public ManageUsers(PageManager pageManager, Manager manager) {
        super(PageTitle.MANAGE_USERS);
        this.manager = manager;
        this.pageManager = pageManager;
    }

    // --- dashboard counts ---
    private void users() {
        var um = pageManager.getUserManager();
        System.out.println(View.BRIGHT_CYAN + "Customers: " + View.RESET + um.getCustomers().size());
        System.out.println(View.BRIGHT_CYAN + "Sellers: " + View.RESET + um.getSellers().size());
        System.out.println(View.BRIGHT_CYAN + "Supporters: " + View.RESET + um.getSupporters().size());
        System.out.println(View.BRIGHT_CYAN + "Managers: " + View.RESET + um.getManagers().size());
        System.out.println("&&& Options &&&");
    }

    private String header() {
        System.out.println(View.BRIGHT_CYAN + "&&& Manage User &&&" + View.RESET);
        users();
        return View.getStringInput(
                View.BRIGHT_WHITE + "1.Edit users\n2.Search\n3.Add\n4.Back\n5.Exit\n"
                        + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
    }

    @Override
    public void show() throws Exception {
        try {
            switch (header()) {
                case "1" -> edit();
                case "2" -> search();
                case "3" -> add();
                case "4" -> pageManager.back();
                case "5" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice" + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    // --- filter & collect ---
    private Role askRoleFilter() {
        System.out.println("Filter by role:");
        System.out.println("1.All  2.Customer  3.Seller  4.Supporter  5.Manager");
        String c = View.getStringInput("Choose: ").trim();
        return switch (c) {
            case "2" -> Role.Customer;
            case "3" -> Role.Seller;
            case "4" -> Role.Supporter;
            case "5" -> Role.Manager;
            default -> Role.User;
        };
    }

    private List<User> collectUsers(Role role) {
        var um = pageManager.getUserManager();
        List<User> res = new ArrayList<>();
        switch (role) {
            case Customer -> res.addAll(um.getCustomers());
            case Seller -> res.addAll(um.getSellers());
            case Supporter -> res.addAll(um.getSupporters());
            case Manager -> res.addAll(um.getManagers());
            case User -> {
                res.addAll(um.getCustomers());
                res.addAll(um.getSellers());
                res.addAll(um.getSupporters());
                res.addAll(um.getManagers());
            }
            default -> {
            }
        }
        return res;
    }

    // --- role/name helpers ---
    private String getNameOf(Object u) {
        if (u instanceof User x) {
            return x.getFirstName() + " " + x.getLastName();
        }
        return "unknown";
    }

    private String getRoleName(User u) {
        if (u instanceof Customer) {
            return "Customer";
        }
        if (u instanceof Seller) {
            return "Seller";
        }
        if (u instanceof Supporter) {
            return "Supporter";
        }
        if (u instanceof Manager) {
            return "Manager";
        }
        return "Unknown";
    }

    // map "username" prompt -> role-specific identifier
    private String getUsernameOf(User u) {
        if (u instanceof Manager m) {
            String v = m.getUsername();              // Manager.username
            return v == null ? "" : v;
        }
        if (u instanceof Supporter s) {
            String v = s.getUserName();              // Supporter.userName
            return v == null ? "" : v;
        }
        if (u instanceof Seller s) {
            String v = s.getStoreCode();             // Seller.storeCode or storeName
            if (v == null || v.isBlank()) {
                v = s.getStoreName();
            }
            return v == null ? "" : v;
        }
        if (u instanceof Customer c) {
            String v = c.getEmail();                 // Customer.email
            return v == null ? "" : v;
        }
        String fn = u.getFirstName() == null ? "" : u.getFirstName();
        String ln = u.getLastName() == null ? "" : u.getLastName();
        return (fn + " " + ln).trim();
    }

    private User pickUserFrom(List<User> list) throws Exception {
        if (list == null || list.isEmpty()) {
            throw new Exception("No user");
        }
        View.extracted(list); // prints indexed list
        int idx = View.getInt("Open (index): ");
        if (idx < 1 || idx > list.size()) {
            throw new Exception("Out of range");
        }
        User u = list.get(idx - 1);
        if (u == null) {
            throw new Exception("Not a User type");
        }
        return u;
    }

    // --- edit flow ---
    private void edit() throws Exception {
        Role rf = askRoleFilter();
        List<User> candidates = collectUsers(rf);
        if (candidates.isEmpty()) {
            System.out.println(View.BRIGHT_YELLOW + "No users." + View.RESET);
            return;
        }
        User target = pickUserFrom(candidates);
        editUserMenu(target);
    }

    private void editUserMenu(User u) throws Exception {
        while (true) {
            System.out.println(View.BRIGHT_CYAN + "=== Edit User ===" + View.RESET);
            System.out.println("Role: " + getRoleName(u));
            System.out.println("First Name: " + u.getFirstName());
            System.out.println("Last Name: " + u.getLastName());
            System.out.println("1.Change first name  2.Change last name  3.Change password 4.Block");
            if (u instanceof Manager m) {
                System.out.println("5.Set rank (0=top)  6.Back");
            } else if (u instanceof Supporter s) {
                System.out.println("5.Assign section (text)  6.Back");
            } else {
                System.out.println("5.Back");
            }
            String c = View.getStringInput("Choose: ").trim();
            if (u instanceof Manager m) {
                if (managerEdit(u, m, c)) {
                    return;
                }
            } else if (u instanceof Supporter s) {
                if (supporterEdit(u, s, c)) {
                    return;
                }
            } else {
                if (userEdit(u, c)) {
                    return;
                }
            }
        }
    }

    private boolean userEdit(User u, String c) throws Exception {
        switch (c) {
            case "1" -> changeFirstName(u);
            case "2" -> changeLastName(u);
            case "3" -> changePassword(u);
            case "4" -> block(u);
            case "5" -> {
                return true;
            } // back
            default -> System.out.println(View.BRIGHT_YELLOW + "Invalid" + View.RESET);
        }
        return false;
    }

    private boolean supporterEdit(User u, Supporter s, String c) throws Exception {
        switch (c) {
            case "1" -> changeFirstName(u);
            case "2" -> changeLastName(u);
            case "3" -> changePassword(u);
            case "4" -> block(u);
            case "5" -> assignSupporterSection(s);
            case "6" -> {
                return true;
            }
            default -> System.out.println(View.BRIGHT_YELLOW + "Invalid" + View.RESET);
        }
        return false;
    }

    private boolean managerEdit(User u, Manager m, String c) throws Exception {
        switch (c) {
            case "1" -> changeFirstName(u);
            case "2" -> changeLastName(u);
            case "3" -> changePassword(u);
            case "4" -> block(u);
            case "5" -> setManagerRank(m);
            case "6" -> {
                return true;
            }
            default -> System.out.println(View.BRIGHT_YELLOW + "Invalid" + View.RESET);
        }
        return false;
    }

    // --- simple field updates ---
    private void changeFirstName(User u) {
        String n = View.getStringInput("New first name: ");
        if (n == null || n.isBlank()) {
            System.out.println(View.BRIGHT_YELLOW + "Empty name" + View.RESET);
            return;
        }
        u.setFirstName(n.trim()); // simple setter
        System.out.println(View.BRIGHT_GREEN + "Updated." + View.RESET);
    }

    private void changeLastName(User u) {
        String un = View.getStringInput("New last name: ").trim();
        if (un.isEmpty()) {
            System.out.println(View.BRIGHT_YELLOW + "Empty last name" + View.RESET);
            return;
        }
        u.setLastName(un);
        System.out.println(View.BRIGHT_GREEN + "Updated." + View.RESET);
    }

    private void changePassword(User u) throws Exception {
        String p = View.getStringInput("New password: ");
        Verify.verifyPassword(p); // project rule
        u.setPassword(p);
        System.out.println(View.BRIGHT_GREEN + "Updated." + View.RESET);
    }

    // --- supporter section (enum from text) ---
    private void assignSupporterSection(Supporter s) {
        String secText = View.getStringInput("Section name (text): ").trim();
        if (secText.isEmpty()) {
            System.out.println(View.BRIGHT_YELLOW + "Empty" + View.RESET);
            return;
        }
        SupportSection sec = null;
        String t = secText.toUpperCase(Locale.ROOT);
        if (t.equals("QUALITY")) {
            sec = SupportSection.QUALITY;
        } else if (t.equals("ORDER")) {
            sec = SupportSection.ORDER;
        } else if (t.equals("SETTINGS")) {
            sec = SupportSection.SETTINGS;
        }

        if (sec == null) {
            System.out.println(View.BRIGHT_YELLOW + "Invalid section" + View.RESET);
            return;
        }
        s.setSection(sec); // assign enum
        System.out.println(View.BRIGHT_GREEN + "Assigned." + View.RESET);
    }

    // --- manager rank (lower = stronger) ---
    private void setManagerRank(Manager m) throws Exception {
        int r = View.getInt("Rank: ");
        if (r < 0) {
            System.out.println(View.BRIGHT_YELLOW + "Invalid rank" + View.RESET);
            return;
        }
        if (!this.manager.canManage(m)) { // permission check
            System.out.println(View.BRIGHT_YELLOW + "You cannot change this manager's rank" + View.RESET);
            return;
        }
        if (r < this.manager.getRank()) { // cannot assign stronger-than-self
            System.out.println(View.BRIGHT_YELLOW + "You cannot assign a stronger rank than yours" + View.RESET);
            return;
        }
        m.setRank(r);
        System.out.println(View.BRIGHT_GREEN + "Updated." + View.RESET);
    }

    // --- search by role-specific "username" key (prompt text kept) ---
    private void search() throws Exception {
        Role rf = askRoleFilter();
        String q = View.getStringInput("Query (username contains, blank=all): ")
                .toLowerCase(Locale.ROOT).trim();

        List<User> list = collectUsers(rf);
        if (!q.isEmpty()) {
            final String qq = q;
            list = list.stream()
                    .filter(u -> getUsernameOf(u).toLowerCase(Locale.ROOT).contains(qq))
                    .toList();
        }
        if (list.isEmpty()) {
            System.out.println(View.BRIGHT_YELLOW + "No match" + View.RESET);
            return;
        }
        User u = pickUserFrom(list);
        editUserMenu(u); // chain to edit
    }

    // --- add menu ---
    private void add() throws Exception {
        System.out.println(View.BRIGHT_CYAN + "=== Add User ===" + View.RESET);
        System.out.println("1.Supporter  2.Manager  3.Back");
        String c = View.getStringInput("Choose: ").trim();
        switch (c) {
            case "1" -> createSupporter();
            case "2" -> createManager();
            case "3" -> {
                return;
            }
            default -> System.out.println(View.BRIGHT_YELLOW + "Invalid" + View.RESET);
        }
    }

    // --- constructors from Users.zip ---
    private void createSupporter() {
        String un = View.getStringInput("Username: ").trim();
        String pw = View.getStringInput("Password (>=8): ");
        String fn = View.getStringInput("First name: ").trim();
        String ln = View.getStringInput("Last name: ").trim();
        String secText = View.getStringInput("Section name (text): ").trim();

        if (fn.isEmpty() || pw.length() < 8) {
            System.out.println(View.BRIGHT_YELLOW + "Bad input" + View.RESET);
            return;
        }
        if (secText.isEmpty()) {
            System.out.println(View.BRIGHT_YELLOW + "Empty" + View.RESET);
            return;
        }

        // map text -> enum
        SupportSection sec = null;
        String t = secText.toUpperCase(Locale.ROOT);
        if (t.equals("QUALITY")) {
            sec = SupportSection.QUALITY;
        } else if (t.equals("ORDER")) {
            sec = SupportSection.ORDER;
        } else if (t.equals("SETTINGS")) {
            sec = SupportSection.SETTINGS;
        }
        if (sec == null) {
            System.out.println(View.BRIGHT_YELLOW + "Invalid section" + View.RESET);
            return;
        }

        try {
            // Supporter(String firstName, String lastName, String userName, String password, SupportSection section)
            Supporter s = new Supporter(fn, ln, un, pw, sec);
            // role is set in constructor
            pageManager.getUserManager().addSupporter(s);
            System.out.println(View.BRIGHT_GREEN + "Supporter created." + View.RESET);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createManager() throws Exception {
        String un = View.getStringInput("Username: ").trim();
        String pw = View.getStringInput("Password (>=8): ");
        String fn = View.getStringInput("First name: ").trim();
        String ln = View.getStringInput("Last name: ").trim();

        // Hierarchy rule: the creator becomes the parent.
        // This prevents accidental rank misuse and fixes "block ancestor" edge cases.

        if (fn.isEmpty() || ln.isEmpty() || pw.length() < 8) {
            System.out.println(View.BRIGHT_YELLOW + "Bad input" + View.RESET);
            return;
        }
        Manager m = new Manager(fn, ln, pw, un, this.manager);
        pageManager.getUserManager().addManager(m);
        System.out.println(View.BRIGHT_GREEN + "Manager created." + View.RESET);
    }

    private void block(User u) throws Exception {
        if (this.manager.canManage(u)) {
            u.block();
        } else {
            throw new Exception(View.BRIGHT_YELLOW + "Cannot block user" + View.RESET);
        }
    }
}
