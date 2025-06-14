package user;

public class Superadmin extends User {
    public Superadmin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Superadmin: " + name + " " + email);
    }
}
