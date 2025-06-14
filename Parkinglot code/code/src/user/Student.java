package user;

public class Student extends User {
    public Student(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Student: " + name + " " + email);
    }
}
