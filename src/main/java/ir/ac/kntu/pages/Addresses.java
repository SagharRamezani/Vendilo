package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.Customer;

import java.util.List;

public class Addresses extends Page {
    private List<Address> addresses;
    private PageManager pageManager;

    public Addresses(PageManager pageManager, Customer customer) {
        super(PageTitle.SUPPORTER_HOME);
        this.pageManager = pageManager;
        this.addresses = customer.getAddresses();
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Addresses &&&" + View.RESET);
            System.out.print(View.BRIGHT_WHITE + "1.View addresses\n2.add address\n3.remove address\n4.Logout\n5.BACK\n6.EXIT\n" + View.BRIGHT_BLACK + "Choose an option: " + View.RESET);
            String choice = scanner().nextLine();
            switch (choice) {
                case "1" -> viewAddresses();
                case "2" -> addAddress();
                case "3" -> removeAddress();
                case "4" -> pageManager.navigateToPage(PageTitle.LOGOUT);
                case "5" -> pageManager.back();
                case "6" -> pageManager.navigateToPage(PageTitle.EXIT);
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }

    public void removeAddress() throws Exception {
        System.out.print("Enter title: ");
        String title = scanner().nextLine().trim();
        addresses.removeIf(address -> address.getTitle().equals(title));
        pageManager.back();
    }

    public void uniTitle(String title) throws Exception {
        for (Address address : this.addresses) {
            if (address.getTitle().equals(title)) {
                throw new Exception(View.BRIGHT_RED + "Duplicate title. Try again." + View.RESET);
            }
        }
    }

    public void uniPostalCode(int postalCode) throws Exception {
        for (Address address : this.addresses) {
            if (address.getPostalCode() == postalCode) {
                throw new Exception(View.BRIGHT_RED + "Duplicate Postal Code. Try again." + View.RESET);
            }
        }
    }

    public void addAddress() throws Exception {
        String title = View.getStringInput("Enter title: ").trim();
        uniTitle(title);
        String state = View.getStringInput("Enter state: ").trim();
        String city = View.getStringInput("Enter city: ").trim();
        String street = View.getStringInput("Enter street: ").trim();
        String alley = View.getStringInput("Enter alley: ").trim();
        int number = Integer.parseInt(View.getStringInput("Enter number: ").trim());
        int unit = Integer.parseInt(View.getStringInput("Enter unit: ").trim());
        int postalCode = Integer.parseInt(View.getStringInput("Enter postal code: ").trim());
        uniPostalCode(postalCode);
        Address address = new Address(title, state, city, street, alley, number, unit, postalCode);
        this.addresses.add(address);
        pageManager.back();
    }

    public void viewAddresses() throws Exception {
        System.out.println(View.BRIGHT_CYAN + "&&& Address list &&&" + View.RESET);
        View.extracted(this.addresses);
        pageManager.back();
    }
}