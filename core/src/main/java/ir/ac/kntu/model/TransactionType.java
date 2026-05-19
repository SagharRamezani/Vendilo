package ir.ac.kntu.model;

public enum TransactionType {
    DEPOSIT, WITHDRAWAL;

    @Override
    public String toString() {
        return switch (this) {
            case DEPOSIT -> "Deposit";
            case WITHDRAWAL -> "Withdrawal";
        };
    }
}
