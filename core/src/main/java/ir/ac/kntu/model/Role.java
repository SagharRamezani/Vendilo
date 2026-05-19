package ir.ac.kntu.model;

public enum Role {
    Customer, Seller, Supporter, Manager, User;

    @Override
    public String toString() {
        return switch (this) {
            case Customer -> "Customer";
            case Seller -> "Seller";
            case Supporter -> "Supporter";
            case Manager -> "Manager";
            case User -> "User";
        };
    }
}