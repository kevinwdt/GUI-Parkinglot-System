package user;

public class FacultyMember extends User {
    public FacultyMember(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Faculty Member: " + name + " " + email);
    }
}