package ir.ac.kntu.model;

import ir.ac.kntu.util.Calendar;

import java.time.LocalDate;
import java.time.ZoneId;

public class Comment {
    private String comment;
    private double rate;
    private LocalDate commentDate;

    public Comment(String comment, double rate) {
        this.comment = comment;
        this.rate = rate;
        this.commentDate = Calendar.now().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public String toString() {
        return "Comment{rate: " + rate + "\ncomment:'" + comment + "\ndate: " + commentDate.toString() + '}';
    }
}
