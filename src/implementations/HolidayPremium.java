package implementations;

import interfaces.PricingStrategy;

public class HolidayPremium implements PricingStrategy {
    private static final double PREMIUM_MULTIPLIER = 1.5; // 50% increase

    @Override
    public double getPrice(double basePrice) {
        return basePrice * PREMIUM_MULTIPLIER;
    }

    @Override
    public String getRuleName() {
        return "Holiday Premium (50% increase)";
    }
}
