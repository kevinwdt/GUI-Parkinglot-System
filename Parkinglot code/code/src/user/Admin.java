package user;

public class Admin extends User {
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + name + " " + email);
    }
}