package interfaces;

public interface PricingStrategy {
    double getPrice(double basePrice);
    String getRuleName();
}
