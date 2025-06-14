package user;

public class NonFacultyMember extends User {
    public NonFacultyMember(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Non-Faculty Member: " + name + " " + email);
    }
}
