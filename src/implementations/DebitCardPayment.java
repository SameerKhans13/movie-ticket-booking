package implementations;

import interfaces.PaymentMethod;

public class DebitCardPayment implements PaymentMethod {
    private String cardNumber;
    private String pin;

    public DebitCardPayment(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment processing
        if (cardNumber != null && !cardNumber.isEmpty() && pin != null && !pin.isEmpty()) {
            System.out.println("Processing debit card payment of: " + amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("Refunding to debit card: " + amount);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "Debit Card";
    }
}
