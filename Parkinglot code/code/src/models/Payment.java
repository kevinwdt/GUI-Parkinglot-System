package models;



import java.time.LocalDate;

import utils.CSVUtils;
import utils.PaymentStrategy;

public class Payment {
    private double amount;
    private PaymentStrategy strategy;
    private String userEmail;

    private static final String FILE_PATH = "data/payments.csv"; // Payment record CSV

    public Payment(double amount, String userEmail) {
        this.amount = amount;
        this.userEmail = userEmail;
    }

    // Set payment strategy
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    // Process the payment
    public void processPayment() {
        if (strategy == null) {
            System.out.println("No payment method selected!");
            return;
        }

        strategy.pay(amount);
        savePaymentToCSV();
    }

    // Save payment info to CSV
    private void savePaymentToCSV() {
        CSVUtils.writeCSV(FILE_PATH, toCSV().split(","));
    }

    // Return CSV-formatted string 
    public String toCSV() {
        String date = LocalDate.now().toString();
        String method = strategy.getClass().getSimpleName().replace("Payment", "");
        return userEmail + "," + date + "," + method + "," + amount;
    }
}

