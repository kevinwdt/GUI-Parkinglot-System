package models;
import gui.AdminFrame;
import utils.CSVUtils;
import java.util.List;
import java.util.stream.Collectors;

public class RealAdminAccess implements AdminAccess {
    private String userEmail;

    public RealAdminAccess(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public void openAdminPanel() {
        new AdminFrame(userEmail); 
    }
}
