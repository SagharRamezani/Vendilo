package ir.ac.kntu.model;

import java.time.Instant;

public class SupportTicket {
    private RequestType subject;
    private String message;
    private User sender;
    private String status;
    private String response;
    private Instant createdAt;

    public SupportTicket(RequestType subject, String message, User sender) {
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.status = "pending";
        this.response = "";
        this.createdAt = Instant.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SupportTicket other)) {
            return false;
        }
        return this.subject == other.subject && this.message.equals(other.message) && this.sender.equals(other.sender);
    }

    @Override
    public String toString() {
        String output = "subject: " + subject.toString() + "\nmessage: " + message + "\nfrom: " + sender + "\nstatus: " + status;
        if (!"pending".equals(status)) {
            output += "\nresponse: " + response;
        }
        return output;
    }

    public void respond(String reply) {
        this.response = reply;
        this.status = "answered";
    }

    public RequestType getSubject() {
        return subject;
    }

    public void setSubject(RequestType subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
