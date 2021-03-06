package cs2340.donationtracker;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import cs2340.donationtracker.Model.Sha256;
import cs2340.donationtracker.Model.User;
import cs2340.donationtracker.Model.User_type;
import cs2340.donationtracker.Model.Verification;

import static org.junit.Assert.*;

/**
 * This is a test method for Donation X android application for team 56B.
 */
@SuppressWarnings("SpellCheckingInspection")
public class DonationXUnitTest {
    private Sha256 hashFunction;
    private Verification verification;

    /**
     * set up method for hash function and verification.
     */
    @Before
    public void setUp() {
        hashFunction = new Sha256();
        verification = new Verification();
    }

    /**
     * This method tests whether passwords are encrypted correctly.
     */
    @SuppressWarnings("AccessStaticViaInstance")
    @Test
    public void testPasswordEncryption() {
        assertEquals("The hash function for Encryption is incorrectly working.",
                "6afeed38373c3bd2d8c26fc811c46309fd94f45cb371ddafc12f47148e47326e",
                hashFunction.encrypt("eo0402"));
    }

    /**
     * This method checks whetehr the form of an email is valid.
     */
    @Test
    public void verifyEmail() {
        assertTrue("The method to verify whether the form of an email is incorrectly working.",
                verification.verifyEmail("haha@gmail.com"));
        assertFalse("The method to verify whether the form of an email is incorrectly working.",
                verification.verifyEmail("hahagmail.com"));
    }


    /**
     * This method checks whether a form of password is valid.
     */
    @Test
    public void verifyPassword() {
        assertTrue("The method to verify whether the form of a password is incorrectly working.",
                verification.verifyPassword("Helloworld"));
        assertFalse("The method to verify whether the form of a password is incorrectly working.",
                verification.verifyPassword("sd"));
    }

    /**
     * This method tests whether users with same username with different type of user appers to
     * be the same.
     */
    @SuppressWarnings("FeatureEnvy")
    @Test
    public void verifyUserEqualsMethodIsOverriden() {
        User a = new User();
        User b = new User();
        a.setType(User_type.ADMIN);
        a.setEmail("X@gmail.com");
        a.setUsername("Daewoong");

        b.setType(User_type.USER);
        b.setEmail("X@gmail.com");
        b.setUsername("Daewoong");
        assertTrue("The equals method in User class is not overriden",a.equals(b));
    }

    /**
     * This method finds a user with user's email.
     */
    @SuppressWarnings({"CollectionDeclaredAsConcreteClass", "FeatureEnvy", "TypeMayBeWeakened"})
    @Test
    public void checkFindByEmail() {
        User a = new User();
        User b = new User();
        User c = new User();
        a.setEmail("a@gmail.com");
        b.setEmail("b@gmail.com");
        c.setEmail("c@gmail.com");
        LinkedList<User> list = new LinkedList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        assertEquals("The method that findByEmail is not correctly working",
                true,
                verification.isValidEmail("b@gmail.com", list));
    }
}