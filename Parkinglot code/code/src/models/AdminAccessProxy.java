package models;
import javax.swing.JOptionPane;
import java.util.List;
import utils.CSVUtils;

public class AdminAccessProxy implements AdminAccess {
    private String userEmail;

    public AdminAccessProxy(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public void openAdminPanel() {
        if (isAdmin(userEmail)) {
            new RealAdminAccess(userEmail).openAdminPanel(); 
        } else {
            JOptionPane.showMessageDialog(null, "You do not have access to the admin panel！");
        }
    }

    private boolean isAdmin(String email) {
        List<String[]> users = CSVUtils.readCSV("data/approved_users.csv");
        for (String[] row : users) {
            if (row.length >= 4) {
                String type = row[0];
                String rowEmail = row[2];
                if (email.equalsIgnoreCase(rowEmail) && type.equalsIgnoreCase("Admin")) {
                    return true;
                }
            }
        }
        return false;
    }
}