package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private String userEmail;

    public MainFrame(String email) {
        this.userEmail = email;
        setTitle("YorkU Parking System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        // Check parking space status
        JButton parkingButton = new JButton("Check parking spaces");
        parkingButton.addActionListener(e -> new ParkingFrame());

        // Reserve or modify 
        JButton bookingButton = new JButton("Reserve/Modify Parking");
        bookingButton.addActionListener(e -> new BookingFrame());

        //  Pay 
        JButton paymentButton = new JButton("Paying for parking");
        paymentButton.addActionListener(e -> new PaymentFrame(userEmail));

        // Admin 
        JButton adminButton = new JButton("Admin Page");
        adminButton.addActionListener(e -> new AdminFrame(userEmail));

        
        add(parkingButton);
        add(bookingButton);
        add(paymentButton);
        add(adminButton);

        setVisible(true);
    }
}

