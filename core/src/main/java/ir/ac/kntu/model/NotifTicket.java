package ir.ac.kntu.model;

public class NotifTicket extends Notification {
    private SupportTicket ticket;

    public NotifTicket(String msg, String body, SupportTicket ticket) {
        super(NotificationType.TICKET, msg, body);
        this.ticket = ticket;
    }

}
