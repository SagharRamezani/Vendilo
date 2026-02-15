package ir.ac.kntu;

public class Verify {
    public static void verifyEmail(String email) throws Exception {
        email = email.toLowerCase();
        if (!email.matches("[a-z0-9]+([._-][a-z]+)*@[a-z]+(\\.[a-z]+)*")) {
            throw new Exception(View.BRIGHT_YELLOW + "Invalid email format" + View.RESET);
        }
    }

    public static void verifyPassword(String password) throws Exception {
        if (password.length() < 8
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*[0-9].*")
                || !password.matches(".*[@#$%~&*_+].*")) {
            throw new Exception(View.YELLOW + "Weak Password" + View.RESET);
        }
    }

    public static void verifyPhone(String phone) throws Exception {
        if (phone.length() != 11 || phone.matches(".*[^0-9].*")) {
            throw new Exception(View.RED + "Invalid Phone Number" + View.RESET);
        }
    }
}
