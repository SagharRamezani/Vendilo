package ir.ac.kntu.model;

import ir.ac.kntu.View;

public class DiscountCode {
    private String code;
    private DiscountType type;
    private double fixedAmount; // Toman
    private int percent; // 0<x<=100
    private int maxUsages; // remaining usages
    private boolean active = true;

    public DiscountCode(String code, double fixedAmount, int maxUsages) {
        this.code = code;
        this.type = DiscountType.FIXED_AMOUNT;
        this.fixedAmount = fixedAmount;
        this.percent = 0;
        this.maxUsages = maxUsages;
    }

    public DiscountCode(String code, int percent, int maxUsages) {
        this.code = code;
        this.type = DiscountType.PERCENTAGE;
        this.percent = percent;
        this.fixedAmount = 0;
        this.maxUsages = maxUsages;
    }

    public String getCode() {
        return code;
    }

    public double calcDiscount(double cartTotal) {
        if (!active) {
            System.out.println(View.BRIGHT_RED + "This code is currently disabled." + View.RESET);
            return 0;
        }
        if (maxUsages <= 0) {
            System.out.println(View.BRIGHT_RED + "This code has no usages." + View.RESET);
            return 0;
        }
        return switch (type) {
            case FIXED_AMOUNT -> {
                if ((fixedAmount * 10 > cartTotal)) {
                    System.out.println(View.BRIGHT_RED + "At least " + (fixedAmount * 10 - cartTotal) + " more funds." + View.RESET);
                    yield 0;
                }
                yield fixedAmount;
            }
            case PERCENTAGE -> (cartTotal / 100) * percent;
        };
    }

    public void consumeOnceIfApplied() {
        maxUsages--;
        if (maxUsages <= 0) {
            active = false;
        }
    }
}
