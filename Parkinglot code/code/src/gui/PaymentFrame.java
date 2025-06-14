package gui;
import javax.swing.*;
import java.awt.*;
import models.Payment;
import user.MaintainUser;
import utils.CreditCardPayment;
import utils.DebitCardPayment;
import utils.MobilePayment;


public class PaymentFrame extends JFrame {
    public PaymentFrame(String userEmail) {
        setTitle("Pay for Parking");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        MaintainUser maintainUser = new MaintainUser();
        String type = "";
        double rate = 10.0;
        try {
            maintainUser.load();
            type = maintainUser.getUserType(userEmail);
            rate = switch (type.toLowerCase()) {
                case "student" -> 5.0;
                case "faculty" -> 8.0;
                case "non-faculty" -> 10.0;
                case "visitor" -> 15.0;
                default -> 10.0;
            };
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Display parking fee
        add(new JLabel("Parking Fee:"));
        JTextField amountField = new JTextField(String.valueOf(rate));
        amountField.setEditable(false);
        add(amountField);

        // Select payment method
        add(new JLabel("Payment Method:"));
        String[] paymentMethods = {"Credit Card", "Mobile Payment", "Debit Card"};
        JComboBox<String> paymentDropdown = new JComboBox<>(paymentMethods);
        add(paymentDropdown);

        // Pay button
        JButton payButton = new JButton("Pay");
        add(payButton);

        double finalRate = rate;
        payButton.addActionListener(e -> {
            Payment payment = new Payment(finalRate, userEmail);

            String selected = (String) paymentDropdown.getSelectedItem();
            if (selected.equals("Credit Card")) {
                payment.setPaymentStrategy(new CreditCardPayment());
            } else if (selected.equals("Mobile Payment")) {
                payment.setPaymentStrategy(new MobilePayment());
            } else {
                payment.setPaymentStrategy(new DebitCardPayment());
            }

            payment.processPayment();
            JOptionPane.showMessageDialog(null, "Payment successful!");
        });

        setVisible(true);
    }
}
