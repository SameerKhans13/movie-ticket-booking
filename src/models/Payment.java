package models;

import enums.PaymentStatus;
import interfaces.PaymentMethod;

public class Payment {
    private String paymentId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;

    public Payment(String paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = PaymentStatus.INITIATED;
    }

    public boolean processPayment() {
        if (method == null) {
            return false;
        }
        boolean success = method.pay(amount);
        this.status = success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        return success;
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
