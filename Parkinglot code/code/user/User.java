package user;

public abstract class User {
    protected String name;
    protected String email;
    protected String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public abstract void displayInfo();

    public void register() {
        System.out.println(name + " registered successfully.");
    }

    public void login() {
        System.out.println(name + " logged in successfully.");
    }

    public void updateProfile(String newName, String newEmail) {
        this.name = newName;
        this.email = newEmail;
        System.out.println(name + " updated profile successfully.");
    }

    public void logout() {
        System.out.println(name + " logged out successfully.");
    }
}
