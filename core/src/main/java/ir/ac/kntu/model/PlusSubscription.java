package ir.ac.kntu.model;

import ir.ac.kntu.util.Calendar;

import java.time.Duration;
import java.time.Instant;

public class PlusSubscription {
    private Instant expiresAt;

    public PlusSubscription(long days) {
        this.expiresAt = Calendar.now().plus(Duration.ofDays(days));
    }

    public void renew(long days) {
        this.expiresAt = this.expiresAt.plus(Duration.ofDays(days));
    }

    public boolean isActive() {
        return Calendar.now().isBefore(expiresAt);
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
