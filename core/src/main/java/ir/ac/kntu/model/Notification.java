package ir.ac.kntu.model;

import ir.ac.kntu.View;
import ir.ac.kntu.util.Calendar;

import java.time.Instant;

public class Notification {
    private NotificationType type;
    private String message;
    private Instant createdAt;
    private String body;
    private boolean read;

    public Notification(NotificationType type, String message, String body) {
        this.type = type;
        this.message = message;
        this.body = body == null ? "" : body;
        this.createdAt = Calendar.now();
        this.read = false;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return read;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + message;
    }

    public void markRead() {
        this.read = true;
    }

    public String view() {
        return message + " [" + type + "]\n" + body + "\n" + createdAt.toString();
    }
}
