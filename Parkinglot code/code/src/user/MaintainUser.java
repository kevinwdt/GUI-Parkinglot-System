package user;

import java.io.FileWriter;
import java.util.ArrayList;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class MaintainUser {
    public ArrayList<User> users = new ArrayList<>();
    public String path = "data/users.csv";

    // Load users from CSV file
    public void load() throws Exception {
        CsvReader reader = new CsvReader(path);
        reader.readHeaders();

        while (reader.readRecord()) {
            String type = reader.get("type");
            String name = reader.get("name");
            String email = reader.get("email");
            String password = reader.get("password");

            User user = UserFactory.createUser(type, name, email, password);
            users.add(user);
        }
        reader.close();
    }

    // Save users to CSV file
    public void update(String path) throws Exception {
        CsvWriter csvOutput = new CsvWriter(new FileWriter(path, false), ',');
        csvOutput.write("type");
        csvOutput.write("name");
        csvOutput.write("email");
        csvOutput.write("password");
        csvOutput.endRecord();

        for (User u : users) {
            csvOutput.write(u.getClass().getSimpleName());
            csvOutput.write(u.getName());
            csvOutput.write(u.getEmail());
            csvOutput.write(u.getPassword());
            csvOutput.endRecord();
        }
        csvOutput.close();
    }

    // Verify user login
    public boolean verifyUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Add new user
    public void addUser(User user) {
        users.add(user);
    }
    public String getUserType(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user.getClass().getSimpleName(); // 返回 Student、Faculty 等
            }
        }
        return null;
    }

}

