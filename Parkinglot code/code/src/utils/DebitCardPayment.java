package utils;

public class DebitCardPayment implements PaymentStrategy {
	 @Override
	 public void pay(double amount) {
	     System.out.println("Pay with DebitCard $" + amount);
	 }
}
