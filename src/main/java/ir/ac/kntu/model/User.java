package ir.ac.kntu.model;

import ir.ac.kntu.Verify;
import ir.ac.kntu.util.PasswordHasher;

public class User {
    private String firstName;
    private String lastName;
    private String password;
    private boolean blocked;
    private Role role;

    public User(String firstName, String lastName, String password) throws Exception {
        this.firstName = firstName;
        this.lastName = lastName;
        setPassword(password);
        this.blocked = false;
        this.role = Role.User;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void block() {
        this.blocked = true;
    }

    public void unblock() {
        this.blocked = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User other)) {
            return false;
        }
        return this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName);
    }

    @Override
    public String toString() {
        return "User{" + firstName + " " + lastName + '}';
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) throws Exception {
        Verify.verifyPassword(password);
        this.password = PasswordHasher.hash(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}