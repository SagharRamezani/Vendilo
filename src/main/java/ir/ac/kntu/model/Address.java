package ir.ac.kntu.model;

public class Address {
    private String title;
    private String state;
    private String city;
    private String street;
    private String alley;
    private int number;
    private int unit;
    private int postalCode;

    public Address(String title, String state, String city, String street, String alley, int number, int unit, int postalCode) {
        this.title = title;
        this.state = state;
        this.city = city;
        this.street = street;
        this.alley = alley;
        this.number = number;
        this.unit = unit;
        this.postalCode = postalCode;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public int getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return title + ": " + state + ", " + city + ", " + street + ", " + (alley.isEmpty() ? "" : alley + ", ") + number + ", " + unit;
    }
}
