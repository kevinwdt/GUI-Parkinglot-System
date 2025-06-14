package utils;

//Credit card payment implementation
public class CreditCardPayment implements PaymentStrategy {
@Override
public void pay(double amount) {
   System.out.println("Pay by credit card $" + amount);
}
}
