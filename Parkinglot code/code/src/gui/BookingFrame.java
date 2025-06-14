package gui;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;



public class BookingFrame extends JFrame {
    private static final String PARKING_SPACES_FILE = "data/parking_spaces.csv";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final double HOURLY_DEPOSIT = 15.0; // $15 deposit per hour
    
    private JComboBox<String> parkingSpotDropdown;
    private JTextField licensePlateField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JButton submitButton;
    private JButton cancelButton;
    private JButton extendButton;

    public BookingFrame() {
        setTitle("Reserve/Modify Parking");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        initializeComponents();
        setupActionListeners();
        setVisible(true);
    }

    private void initializeComponents() {
        // Parking space selection - Load from parking_spaces.csv
        add(new JLabel("Select a parking space:"));
        String[] availableSpots = getAvailableParkingSpots();
        parkingSpotDropdown = new JComboBox<>(availableSpots);
        add(parkingSpotDropdown);

        // License plate input
        add(new JLabel("License Plate Number:"));
        licensePlateField = new JTextField();
        add(licensePlateField);

        // Start time
        add(new JLabel("Start time (HH:mm):"));
        startTimeField = new JTextField();
        add(startTimeField);

        // End time
        add(new JLabel("End time (HH:mm):"));
        endTimeField = new JTextField();
        add(endTimeField);

        // Submit reservation button
        submitButton = new JButton("Submit Reservation");
        add(submitButton);

        // Cancel booking button
        cancelButton = new JButton("Cancel Booking");
        add(cancelButton);

        // Extend booking button
        extendButton = new JButton("Extend Booking");
        add(extendButton);
    }

    private String[] getAvailableParkingSpots() {
        List<String> availableSpots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PARKING_SPACES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].equalsIgnoreCase("Available")) {
                    availableSpots.add(parts[0] + " - " + parts[1]); // Format: "Lot1 - P1"
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to default spots if file can't be read
            return new String[]{"Lot1 - P1", "Lot1 - P2", "Lot2 - P1", "Lot2 - P2"};
        }
        return availableSpots.toArray(new String[0]);
    }

    private void setupActionListeners() {
        submitButton.addActionListener(e -> handleBooking());
        cancelButton.addActionListener(e -> handleCancel());
        extendButton.addActionListener(e -> handleExtension());
        
        //Print the current time
        LocalTime now = LocalTime.now();
        System.out.println(now);
    }

    private void handleBooking() {
        String spot = (String) parkingSpotDropdown.getSelectedItem();
        String licensePlate = licensePlateField.getText().trim();
        String startTimeStr = startTimeField.getText().trim();
        String endTimeStr = endTimeField.getText().trim();

        if (licensePlate.isEmpty() || startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            showError("All fields must be filled!");
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(startTimeStr, TIME_FORMATTER);
            LocalTime endTime = LocalTime.parse(endTimeStr, TIME_FORMATTER);

            if (!endTime.isAfter(startTime)) {
                showError("End time must be after start time!");
                return;
            }

            if (!isParkingAvailable(spot, startTimeStr, endTimeStr)) {
                showError("Parking space " + spot + " is already booked for this time!");
                return;
            }

            // Show deposit information
            int confirm = JOptionPane.showConfirmDialog(this,
                "A deposit of $" + HOURLY_DEPOSIT + " will be charged.\n" +
                "If you don't arrive within 1 hour of your booking time,\n" +
                "this deposit will not be refunded.",
                "Deposit Information",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

            if (confirm != JOptionPane.OK_OPTION) {
                return; // User cancelled
            }

            if (bookParkingSpace(spot, licensePlate, startTimeStr, endTimeStr)) {
                showSuccess("Parking " + spot + " booked successfully!\n" +
                          "Time: " + startTimeStr + " - " + endTimeStr + "\n" +
                          "$" + HOURLY_DEPOSIT + " deposit charged.");
            } else {
                showError("Failed to book parking space");
            }

        } catch (DateTimeParseException e) {
            showError("Invalid time format! Please use HH:mm format");
        }
    }

    private boolean bookParkingSpace(String spot, String licensePlate, String startTime, String endTime) {
        List<String[]> allSpaces = readAllParkingSpaces();
        for (String[] space : allSpaces) {
            if ((space[0] + " - " + space[1]).equals(spot)) {
                if (space[2].equalsIgnoreCase("Available")) {
                    space[2] = "Occupied";
                    space[3] = licensePlate;
                    space[4] = startTime;
                    space[5] = endTime;
                    return writeAllParkingSpaces(allSpaces);
                }
            }
        }
        return false;
    }

    private void handleCancel() {
        String licensePlate = licensePlateField.getText().trim();
        if (licensePlate.isEmpty()) {
            showError("Please enter your license plate number");
            return;
        }

        String spot = (String) parkingSpotDropdown.getSelectedItem();
        if (cancelParkingBooking(spot, licensePlate)) {
            // Success message is now handled within cancelParkingBooking
            // Clear fields after successful cancellation
            licensePlateField.setText("");
            startTimeField.setText("");
            endTimeField.setText("");
        } else {
            showError("Failed to cancel booking or no booking found");
        }
    }

    private boolean cancelParkingBooking(String spot, String licensePlate) {
        List<String[]> allSpaces = readAllParkingSpaces();
        for (String[] space : allSpaces) {
            if ((space[0] + " - " + space[1]).equals(spot) && 
                space.length >= 6 && 
                space[3].equalsIgnoreCase(licensePlate)) {
                
                try {
                    LocalTime startTime = LocalTime.parse(space[4], TIME_FORMATTER);
                    LocalTime now = LocalTime.now();
                    
                    if (now.isAfter(startTime)) {
                        // This is now acting as check-out
                        if (now.isAfter(startTime.plusHours(1))) {
                            showMessage("Checked out. Deposit of $" + HOURLY_DEPOSIT + " not refunded (no-show).");
                        } else {
                            showMessage("Checked out. Your $" + HOURLY_DEPOSIT + " deposit has been refunded.");
                        }
                    } else {
                        // Standard cancellation before start time
                        showMessage("Booking cancelled. Your $" + HOURLY_DEPOSIT + " deposit has been refunded.");
                    }

                    space[2] = "Available";
                    space[3] = "";
                    space[4] = "";
                    space[5] = "";
                    return writeAllParkingSpaces(allSpaces);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleExtension() {
        String licensePlate = licensePlateField.getText().trim();
        if (licensePlate.isEmpty()) {
            showError("Please enter your license plate number");
            return;
        }

        String spot = (String) parkingSpotDropdown.getSelectedItem();
        String[] booking = findBooking(spot, licensePlate);

        if (booking == null) {
            showError("No active booking found");
            return;
        }

        try {
            LocalTime currentEnd = LocalTime.parse(booking[5], TIME_FORMATTER);
            LocalTime now = LocalTime.now();
            
            if (now.isAfter(currentEnd)) {
                showError("Cannot extend booking after it has ended");
                return;
            }

            String newEndTime = JOptionPane.showInputDialog(this, 
                "Current end time: " + booking[5] + "\nEnter new end time (HH:mm):",
                currentEnd.plusHours(1).format(TIME_FORMATTER));
            
            if (newEndTime == null || newEndTime.trim().isEmpty()) return;

            LocalTime newEnd = LocalTime.parse(newEndTime.trim(), TIME_FORMATTER);
            if (!newEnd.isAfter(currentEnd)) {
                showError("New end time must be after current end time");
                return;
            }

            if (extendParkingBooking(spot, licensePlate, newEndTime)) {
                showSuccess("Booking extended successfully!\nNew end time: " + newEndTime);
                endTimeField.setText(newEndTime);
            } else {
                showError("Failed to extend booking");
            }
        } catch (DateTimeParseException e) {
            showError("Invalid time format! Please use HH:mm format");
        }
    }

    private boolean extendParkingBooking(String spot, String licensePlate, String newEndTime) {
        List<String[]> allSpaces = readAllParkingSpaces();
        for (String[] space : allSpaces) {
            if ((space[0] + " - " + space[1]).equals(spot) && 
                space.length >= 6 && 
                space[3].equalsIgnoreCase(licensePlate)) {
                
                space[5] = newEndTime;
                return writeAllParkingSpaces(allSpaces);
            }
        }
        return false;
    }

    private String[] findBooking(String spot, String licensePlate) {
        List<String[]> allSpaces = readAllParkingSpaces();
        for (String[] space : allSpaces) {
            if ((space[0] + " - " + space[1]).equals(spot) && 
                space.length >= 6 && 
                space[3].equalsIgnoreCase(licensePlate)) {
                return space;
            }
        }
        return null;
    }

    private boolean isParkingAvailable(String spot, String newStart, String newEnd) {
        List<String[]> allSpaces = readAllParkingSpaces();
        for (String[] space : allSpaces) {
            if ((space[0] + " - " + space[1]).equals(spot)) {
                if (space[2].equalsIgnoreCase("Occupied") && space.length >= 6) {
                    if (isOverlapping(newStart, newEnd, space[4], space[5])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isOverlapping(String newStart, String newEnd, String existingStart, String existingEnd) {
        try {
            LocalTime newStartTime = LocalTime.parse(newStart, TIME_FORMATTER);
            LocalTime newEndTime = LocalTime.parse(newEnd, TIME_FORMATTER);
            LocalTime existingStartTime = LocalTime.parse(existingStart, TIME_FORMATTER);
            LocalTime existingEndTime = LocalTime.parse(existingEnd, TIME_FORMATTER);

            return !(newEndTime.isBefore(existingStartTime) || newStartTime.isAfter(existingEndTime));
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private List<String[]> readAllParkingSpaces() {
        List<String[]> spaces = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PARKING_SPACES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Ensure array has at least 6 elements
                String[] space = new String[6];
                System.arraycopy(parts, 0, space, 0, Math.min(parts.length, space.length));
                spaces.add(space);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return spaces;
    }

    private boolean writeAllParkingSpaces(List<String[]> spaces) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PARKING_SPACES_FILE))) {
            for (String[] space : spaces) {
                bw.write(String.join(",", space));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}