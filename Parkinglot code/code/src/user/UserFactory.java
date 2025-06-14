package user;

public class UserFactory {
    public static User createUser(String type, String name, String email, String password) {
        switch (type.toLowerCase()) {
            case "student":
                return new Student(name, email, password);
            case "faculty":
                return new FacultyMember(name, email, password);
            case "non-faculty":
                return new NonFacultyMember(name, email, password);
            case "visitor":
                return new Visitor(name, email, password);
            case "admin":
            	return new Admin(name, email, password);
            case "superadmin":
            	return new Superadmin(name, email, password);
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }
}