package implementations;

import interfaces.PaymentMethod;

public class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment processing
        if (cardNumber != null && !cardNumber.isEmpty() && cvv != null && !cvv.isEmpty()) {
            System.out.println("Processing credit card payment of: " + amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("Refunding credit card: " + amount);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "Credit Card";
    }
}
