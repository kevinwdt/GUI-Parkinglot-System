package gui;
import javax.swing.*;

import models.Manager;
import utils.CSVUtils;
import models.AdminAccessProxy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminFrame extends JFrame {
    private String userEmail;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JButton approveButton, removeButton, viewParkingButton, viewPaymentsButton, refundButton;

    public AdminFrame(String userEmail) {
        this.userEmail = userEmail;

        // Check if user is an admin
        if (!isAdmin(userEmail) && !isSuperAdmin(userEmail)) {
            JOptionPane.showMessageDialog(null, "You are not admin. Access denied.");
            dispose(); 
            return;
        }

        setTitle("Admin Management");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // List panel
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane scrollPane = new JScrollPane(userList);
        add(scrollPane, BorderLayout.CENTER);

        // Button area
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 2, 10, 10));

        JButton viewApprovedUsersButton = new JButton("View Approved Users");
        JButton approveButton = new JButton("Approve User");
        JButton removeButton = new JButton("Remove User");
        JButton viewParkingButton = new JButton("View Parking Spaces");
        JButton viewPaymentsButton = new JButton("View Payment Records");
        JButton viewLotsButton = new JButton("View Parking Lots");
        JButton enableLotButton = new JButton("Enable Parking Lot");
        JButton disableLotButton = new JButton("Disable Parking Lot");
        JButton addSpaceButton = new JButton("Add Parking Space");
        JButton disableSpaceButton = new JButton("Disable Parking Space");
        JButton viewSpacesInLotButton = new JButton("View Spaces in Lot");
        JButton generateAdminButton = new JButton("Generate Admin Account");
        generateAdminButton.addActionListener(e -> {
            if (isSuperAdmin(userEmail)) {
                Manager.getInstance().generateAdminAccount();
            } else {
                JOptionPane.showMessageDialog(null, "You are not superadmin. Access denied.");
            }
        });

        buttonPanel.add(viewApprovedUsersButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(viewParkingButton);
        buttonPanel.add(viewPaymentsButton);
        buttonPanel.add(viewLotsButton);
        buttonPanel.add(enableLotButton);
        buttonPanel.add(disableLotButton);
        buttonPanel.add(disableSpaceButton);
        buttonPanel.add(addSpaceButton);
        buttonPanel.add(viewSpacesInLotButton);
        buttonPanel.add(generateAdminButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event bindings
        viewApprovedUsersButton.addActionListener(e -> loadApprovedUsers());
        approveButton.addActionListener(new ApproveUserAction());
        removeButton.addActionListener(new RemoveUserAction());
        viewParkingButton.addActionListener(e -> loadParkingStatus());
        viewPaymentsButton.addActionListener(e -> loadPayments());
        viewLotsButton.addActionListener(e -> loadParkingLots());
        enableLotButton.addActionListener(e -> enableParkingLot());
        disableLotButton.addActionListener(e -> disableParkingLot());
        addSpaceButton.addActionListener(e -> addParkingSpace());
        disableSpaceButton.addActionListener(e -> disableParkingSpace());
        viewSpacesInLotButton.addActionListener(e -> viewSpacesInLot());

        // Load users pending approval
        loadPendingUsers();

        setVisible(true);
    }

    private boolean isAdmin(String email) {
        List<String[]> users = CSVUtils.readCSV("data/approved_users.csv");
        for (String[] row : users) {
            if (row.length >= 4) {
                String type = row[0];
                String userEmail = row[2];
                if (email.equalsIgnoreCase(userEmail) && type.equalsIgnoreCase("Admin")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSuperAdmin(String email) {
        List<String[]> users = CSVUtils.readCSV("data/approved_users.csv");
        for (String[] row : users) {
            if (row.length >= 4) {
                String type = row[0];
                String rowEmail = row[2];
                if (email.equalsIgnoreCase(rowEmail) && type.equalsIgnoreCase("SuperAdmin")) {
                    return true;
                }
            }
        }
        return false;
    }

    //Load pending users
    private void loadPendingUsers() {
        userListModel.clear();
        List<String[]> users = CSVUtils.readCSV("data/users.csv");
        for (String[] user : users) {
            if (user.length >= 3) {
                userListModel.addElement(user[0] + " - " + user[1] + " - " + user[2]);
            }
        }
    }

    //  Load approved users
    private void loadApprovedUsers() {
        userListModel.clear();
        List<String[]> users = CSVUtils.readCSV("data/approved_users.csv");
        for (String[] user : users) {
            if (user.length >= 3) {
                String display = user[0] + " - " + user[1] + " - " + user[2];
                userListModel.addElement(display);
            }
        }
    }

    // Approve user button event
    class ApproveUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {
                String userEmail = userListModel.get(selectedIndex).split(" - ")[0];
                Manager.getInstance().approveUser(userEmail);
                JOptionPane.showMessageDialog(null, "User approved: " + userEmail);
                userListModel.remove(selectedIndex); // Refresh list
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to approve.");
            }
        }
    }

    // Remove user button event
    class RemoveUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {
                String userEmail = userListModel.get(selectedIndex).split(" - ")[0];
                Manager.getInstance().removeUser(userEmail);
                JOptionPane.showMessageDialog(null, "User removed: " + userEmail);
                loadPendingUsers(); // Refresh list
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to remove.");
            }
        }
    }

    // Load payment records
    private void loadPayments() {
        userListModel.clear();
        List<String[]> payments = CSVUtils.readCSV("data/payments.csv");
        for (String[] payment : payments) {
            if (payment.length >= 4) {
                String display = String.format("%s - %s - %s - %s",
                        payment[0], payment[1], payment[2], payment[3]);
                userListModel.addElement(display);
            }
        }
    }

    // Show parking space list
    private void loadParkingStatus() {
        userListModel.clear();
        List<String[]> spaces = CSVUtils.readCSV("data/parking_spaces.csv");
        for (String[] row : spaces) {
            if (row.length >= 3) {
                String display = row[0] + " - " + row[1] + " - " + row[2];
                userListModel.addElement(display);
            }
        }
    }

    // Show parking lots
    private void loadParkingLots() {
        userListModel.clear();
        List<String[]> lots = CSVUtils.readCSV("data/parking_lots.csv");
        for (String[] lot : lots) {
            if (lot.length >= 2) {
                String display = lot[0] + " - " + lot[1];
                userListModel.addElement(display);
            }
        }
    }

    // Enable parking lot
    private void enableParkingLot() {
        String lotId = JOptionPane.showInputDialog("Enter the lot ID to enable:");
        if (lotId != null && !lotId.trim().isEmpty()) {
            List<String[]> lots = CSVUtils.readCSV("data/parking_lots.csv");
            List<String[]> spaces = CSVUtils.readCSV("data/parking_spaces.csv");
            boolean found = false;

            for (String[] lot : lots) {
                if (lot[0].equalsIgnoreCase(lotId.trim())) {
                    lot[1] = "Available";
                    found = true;
                }
            }

            for (String[] space : spaces) {
                if (space[0].equalsIgnoreCase(lotId.trim())) {
                    space[2] = "Available";
                }
            }

            if (found) {
                CSVUtils.writeCSV("data/parking_lots.csv", lots);
                CSVUtils.writeCSV("data/parking_spaces.csv", spaces);
                JOptionPane.showMessageDialog(null, "Lot enabled: " + lotId);
                loadParkingLots();
            } else {
                JOptionPane.showMessageDialog(null, "Lot not found.");
            }
        }
    }

    // Disable parking lot
    private void disableParkingLot() {
        String lotId = JOptionPane.showInputDialog("Enter the lot ID to disable:");
        if (lotId != null && !lotId.trim().isEmpty()) {
            List<String[]> lots = CSVUtils.readCSV("data/parking_lots.csv");
            List<String[]> spaces = CSVUtils.readCSV("data/parking_spaces.csv");
            boolean found = false;

            for (String[] lot : lots) {
                if (lot[0].equalsIgnoreCase(lotId.trim())) {
                    lot[1] = "Unavailable";
                    found = true;
                }
            }

            for (String[] space : spaces) {
                if (space[0].equalsIgnoreCase(lotId.trim())) {
                    space[2] = "Unavailable";
                }
            }

            if (found) {
                CSVUtils.writeCSV("data/parking_lots.csv", lots);
                CSVUtils.writeCSV("data/parking_spaces.csv", spaces);
                JOptionPane.showMessageDialog(null, "Lot disabled: " + lotId);
                loadParkingLots();
            } else {
                JOptionPane.showMessageDialog(null, "Lot not found.");
            }
        }
    }

    // Add parking space
    private void addParkingSpace() {
        String lotId = JOptionPane.showInputDialog("Enter lot ID (e.g., Lot1):");
        String spaceId = JOptionPane.showInputDialog("Enter space ID (e.g., P1):");
        if (lotId != null && spaceId != null &&
                !lotId.trim().isEmpty() && !spaceId.trim().isEmpty()) {
            CSVUtils.writeCSV("data/parking_spaces.csv",
                    new String[]{lotId.trim(), spaceId.trim(), "Available"});
            JOptionPane.showMessageDialog(null, "Added space: " + lotId + "-" + spaceId);
        }
    }

    // Disable parking space
    private void disableParkingSpace() {
        String fullId = JOptionPane.showInputDialog("Enter space to disable (format Lot1-P1):");
        if (fullId != null && fullId.contains("-")) {
            String[] parts = fullId.split("-");
            String lotId = parts[0].trim();
            String spaceId = parts[1].trim();
            List<String[]> spaces = CSVUtils.readCSV("data/parking_spaces.csv");
            boolean found = false;

            for (String[] space : spaces) {
                if (space[0].equalsIgnoreCase(lotId) && space[1].equalsIgnoreCase(spaceId)) {
                    space[2] = "Unavailable";
                    found = true;
                }
            }

            if (found) {
                CSVUtils.writeCSV("data/parking_spaces.csv", spaces);
                JOptionPane.showMessageDialog(null, "Space disabled: " + fullId);
                loadParkingStatus();
            } else {
                JOptionPane.showMessageDialog(null, "Specified space not found.");
            }
        }
    }

    // View spaces in a specific lot
    private void viewSpacesInLot() {
        String lotId = JOptionPane.showInputDialog("Enter lot ID to view spaces:");
        if (lotId != null && !lotId.trim().isEmpty()) {
            userListModel.clear();
            List<String[]> spaces = CSVUtils.readCSV("data/parking_spaces.csv");
            for (String[] space : spaces) {
                if (space.length >= 3 && space[0].equalsIgnoreCase(lotId.trim())) {
                    String display = String.format("%s - %s - %s", space[0], space[1], space[2]);
                    userListModel.addElement(display);
                }
            }
        }
    }
}