package implementations;

import interfaces.PricingStrategy;

public class StandardPricing implements PricingStrategy {
    @Override
    public double getPrice(double basePrice) {
        return basePrice;
    }

    @Override
    public String getRuleName() {
        return "Standard Pricing";
    }
}
