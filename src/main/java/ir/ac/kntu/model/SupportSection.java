package ir.ac.kntu.model;

public enum SupportSection {
    QUALITY, ORDER, SETTINGS;

    @Override
    public String toString() {
        return switch (this) {
            case QUALITY -> "Quality";
            case ORDER -> "Order";
            case SETTINGS -> "Settings";
        };
    }
}
