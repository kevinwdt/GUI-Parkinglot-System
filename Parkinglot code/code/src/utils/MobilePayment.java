package utils;

public class MobilePayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Pay using mobile payment $" + amount);
    }
}
