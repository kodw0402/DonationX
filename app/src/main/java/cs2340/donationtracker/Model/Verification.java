package cs2340.donationtracker.Model;

import java.util.Collection;
import java.util.regex.Pattern;

public class Verification {
    @SuppressWarnings("TypeMayBeWeakened")
    public boolean verifyPassword(String password) {
        return (password != null) && (password.length() >= 6);
    }
    @SuppressWarnings({"TypeMayBeWeakened", "ChainedMethodCall"})
    public boolean verifyEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return (email != null) && pat.matcher(email).matches();
    }
    @SuppressWarnings("TypeMayBeWeakened")
    public boolean isValidEmail(String email, Collection<User> users) {
        for (User s : users) {
            if (email.equals(s.getEmail())) {
                return true;
            }
        }
        return false;
    }
}