package ir.ac.kntu.model;

public enum TicketStatus {
    OPEN, ANSWERED, REJECTED, CLOSED;

    @Override
    public String toString() {
        return switch (this) {
            case OPEN -> "Open";
            case ANSWERED -> "Answered";
            case REJECTED -> "Rejected";
            case CLOSED -> "Closed";
        };
    }
}
