package models;
import utils.CSVUtils;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class Manager {
    private static Manager instance;
    private String name;
    private String email;

    private static final String USERS_FILE = "data/users.csv";                // Pending users
    private static final String APPROVED_USERS_FILE = "data/approved_users.csv"; // Approved users
    private static final String PARKING_FILE = "data/parking_lots.csv";       // Parking lot info
    private static final String PAYMENTS_FILE = "data/payments.csv";          // Payment records

    private Manager() {
        this.name = "Admin";
        this.email = "admin@yorku.ca";
    }

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    //Approve user registration 
    public void approveUser(String userEmail) {
        List<String[]> users = CSVUtils.readCSV(USERS_FILE);
        List<String[]> approvedUsers = users.stream()
                .filter(user -> user[0].equals(userEmail))
                .collect(Collectors.toList());

        if (!approvedUsers.isEmpty()) {
            CSVUtils.writeCSV(APPROVED_USERS_FILE, approvedUsers.get(0));
            System.out.println("User " + userEmail + " has been approved.");
        } else {
            System.out.println("User " + userEmail + " not found.");
        }
    }

    // View all approved users 
    public void viewApprovedUsers() {
        List<String[]> approvedUsers = CSVUtils.readCSV(APPROVED_USERS_FILE);
        System.out.println("List of approved users:");
        for (String[] user : approvedUsers) {
            System.out.println(user[0]);
        }
    }

    //Remove user 
    public void removeUser(String userEmail) {
        List<String[]> approvedUsers = CSVUtils.readCSV(APPROVED_USERS_FILE);
        List<String[]> updatedUsers = approvedUsers.stream()
                .filter(user -> !user[0].equals(userEmail))
                .collect(Collectors.toList());

        CSVUtils.writeCSV(APPROVED_USERS_FILE, updatedUsers);
        System.out.println("User " + userEmail + " has been removed.");
    }

    // View all parking lot statuses
    public void viewParkingStatus() {
        List<String[]> parkingLots = CSVUtils.readCSV(PARKING_FILE);
        System.out.println("Parking Lot Status:");
        for (String[] lot : parkingLots) {
            System.out.println("Lot ID: " + lot[0] + " Status: " + lot[1]);
        }
    }

    //  Enable/Disable parking lot 
    public void updateParkingStatus(String parkingID, String newStatus) {
        List<String[]> parkingLots = CSVUtils.readCSV(PARKING_FILE);
        for (String[] lot : parkingLots) {
            if (lot[0].equals(parkingID)) {
                lot[1] = newStatus;
                System.out.println("Parking lot " + parkingID + " status updated to: " + newStatus);
                break;
            }
        }
        CSVUtils.writeCSV(PARKING_FILE, parkingLots);
    }

    // View all payment records 
    public void viewPayments() {
        List<String[]> payments = CSVUtils.readCSV(PAYMENTS_FILE);
        System.out.println("Payment Records:");
        for (String[] payment : payments) {
            System.out.println("User: " + payment[0] + " Amount: $" + payment[1]);
        }
    }

    //7. Refund user
    public void refundUser(String userEmail) {
        List<String[]> payments = CSVUtils.readCSV(PAYMENTS_FILE);
        List<String[]> updatedPayments = payments.stream()
                .filter(payment -> !payment[0].equals(userEmail))
                .collect(Collectors.toList());

        CSVUtils.writeCSV(PAYMENTS_FILE, updatedPayments);
        System.out.println("Payment for user " + userEmail + " has been refunded.");
    }

    // Generate new admin account (SuperAdmin only)
    public void generateAdminAccount() {
        String username = "admin" + UUID.randomUUID().toString().substring(0, 5);
        String email = username + "@yorku.ca";
        String password = UUID.randomUUID().toString().substring(0, 8) + "#A";

        
        CSVUtils.writeCSV(APPROVED_USERS_FILE, new String[]{"Admin", username, email, password});

        JOptionPane.showMessageDialog(null,
                "New admin account generated:\nUsername: " + username +
                        "\nEmail: " + email +
                        "\nPassword: " + password,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}


