package ir.ac.kntu.model;

public enum RequestType {
    QUALITY_ISSUE,
    ORDER_MISMATCH,
    SETTINGS,
    ORDER_NOT_RECEIVED;

    @Override
    public String toString() {
        return switch (this) {
            case QUALITY_ISSUE -> "Quality Issue";
            case ORDER_MISMATCH -> "Order Mismatch";
            case ORDER_NOT_RECEIVED -> "Order Not Received";
            case SETTINGS -> "Settings";
        };
    }
}
