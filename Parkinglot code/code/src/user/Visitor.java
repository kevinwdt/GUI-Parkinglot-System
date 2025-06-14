package user;

public class Visitor extends User {
    public Visitor(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Visitor: " + name + " " + email);
    }
}