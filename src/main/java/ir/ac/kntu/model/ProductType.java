package ir.ac.kntu.model;

public enum ProductType {
    PRODUCT, BOOK, DIGITAL, LAPTOP, MOBILE;

    @Override
    public String toString() {
        return switch (this) {
            case BOOK -> "Book";
            case DIGITAL -> "Digital";
            case LAPTOP -> "Laptop";
            case MOBILE -> "Mobile";
            default -> "Unknown";
        };
    }
}
