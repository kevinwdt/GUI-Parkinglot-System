package gui;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class ParkingFrame extends JFrame {
    public ParkingFrame() {
        setTitle("Parking space status");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // CSV 文件路径
        String lotCSV = "data/parking_lots.csv";
        String spaceCSV = "data/parking_spaces.csv";

        // 读取并显示停车状态
        String[] parkingStatus = readParkingStatus(lotCSV, spaceCSV);
        JList<String> parkingList = new JList<>(parkingStatus);
        add(new JScrollPane(parkingList));

        setVisible(true);
    }
private String[] readParkingStatus(String lotFile, String spaceFile) {
    Set<String> availableLots = new HashSet<>();
    List<String> result = new ArrayList<>();

    // 读取停车场状态
    try (BufferedReader br = new BufferedReader(new FileReader(lotFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2 && parts[1].trim().equalsIgnoreCase("Available")) {
                availableLots.add(parts[0].trim());
            }
        }
    } catch (IOException e) {
        showError("读取 parking_lots.csv 时出错");
        e.printStackTrace();
        return new String[] {};
    }

    // 读取停车位状态，新增支持车牌扫描显示
    try (BufferedReader br = new BufferedReader(new FileReader(spaceFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String lotName = parts[0].trim();
                String spaceNumber = parts[1].trim();
                String status = parts[2].trim();
                String plate = (parts.length >= 4) ? parts[3].trim() : "";

                if (availableLots.contains(lotName)) {
                    String entry = "Parking space " + spaceNumber + ": " + status;
                    if (status.equalsIgnoreCase("Occupied") && !plate.isEmpty()) {
                        entry += " - Plate: " + plate;
                    }
                    result.add(entry);
                }
            }
        }
    } catch (IOException e) {
        showError("读取 parking_spaces.csv 时出错");
        e.printStackTrace();
    }

    return result.toArray(new String[0]);
}
private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
}
}
