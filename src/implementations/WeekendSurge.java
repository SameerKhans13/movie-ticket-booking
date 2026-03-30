package implementations;

import interfaces.PricingStrategy;

public class WeekendSurge implements PricingStrategy {
    private static final double SURGE_MULTIPLIER = 1.2; // 20% increase

    @Override
    public double getPrice(double basePrice) {
        return basePrice * SURGE_MULTIPLIER;
    }

    @Override
    public String getRuleName() {
        return "Weekend Surge (20% increase)";
    }
}
