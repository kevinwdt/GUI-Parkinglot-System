package utils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import models.Payment;

public class CSVUtils {
   
    private static final String USERS_FILE = "data/users.csv";
    private static final String APPROVED_USERS_FILE = "data/approved_users.csv";
    private static final String PAYMENTS_FILE = "data/payments.csv";
    private static final String PARKING_FILE = "data/parking_lots.csv";

    // Read CSV file
    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(",")); 
            }
        } catch (IOException e) {
            System.out.println("Failed to read CSV: " + e.getMessage());
        }
        return data;
    }

    // Append user data to users.csv
    public static void writeUserToPending(String[] userData) {
        writeCSV(USERS_FILE, userData);
    }

    // Remove from users.csv and write to approved_users.csv
    public static void approveUser(String email) {
        List<String[]> allUsers = readCSV(USERS_FILE);
        List<String[]> updatedUsers = new ArrayList<>();

        boolean approved = false;

        for (String[] user : allUsers) {
            if (user[0].equalsIgnoreCase(email)) {
                writeCSV(APPROVED_USERS_FILE, user);  // Write to approved_users.csv
                approved = true;
            } else {
                updatedUsers.add(user);
            }
        }

        if (approved) {
            writeCSV(USERS_FILE, updatedUsers); // Overwrite users.csv to remove approved user
            System.out.println("User approved: " + email);
        } else {
            System.out.println("User not found: " + email);
        }
    }

    public static void savePayment(Payment p) {
        String[] row = p.toCSV().split(",");
        writeCSV("data/payments.csv", row);
    }

    //  Write a single row to CSV
    public static void writeCSV(String filePath, String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(",", data));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Failed to write to CSV: " + e.getMessage());
        }
    }

    // Write entire CSV
    public static void writeCSV(String filePath, List<String[]> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) { // false = overwrite
            for (String[] line : data) {
                bw.write(String.join(",", line));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to overwrite CSV: " + e.getMessage());
        }
    }
}

