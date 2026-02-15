package ir.ac.kntu.model;

public class StockWatch extends Notification {
    private Product product;

    public StockWatch(String msg, String body, Product product) {
        super(NotificationType.STOCK, msg, body);
        this.product = product;
    }

}
