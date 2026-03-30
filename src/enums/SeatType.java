package enums;

public enum SeatType {
    SILVER(1.0),
    GOLD(1.5),
    PLATINUM(2.0),
    VIP(3.0);

    private final double priceMultiplier;

    SeatType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getMultiplier() {
        return priceMultiplier;
    }
}
