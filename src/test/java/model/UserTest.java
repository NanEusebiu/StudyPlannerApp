package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    private final int TEST_ID = 1;
    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "pass123";
    private final String TEST_FULLNAME = "Test User";
    private final String TEST_ROLE = "student";

    @BeforeEach
    void setUp() {
        user = new User(TEST_ID, TEST_USERNAME, TEST_PASSWORD, TEST_FULLNAME, TEST_ROLE);
    }

    @Test
    void testUserConstructor() {
        assertNotNull(user, "User object should be created");
        assertEquals(TEST_ID, user.getId(), "ID should match constructor argument");
        assertEquals(TEST_USERNAME, user.getUsername(), "Username should match constructor argument");
        assertEquals(TEST_PASSWORD, user.getPassword(), "Password should match constructor argument");
        assertEquals(TEST_FULLNAME, user.getFullName(), "Full name should match constructor argument");
        assertEquals(TEST_ROLE, user.getRole(), "Role should match constructor argument");
    }

    @Test
    void testGetId() {
        assertEquals(TEST_ID, user.getId(), "getId should return correct ID");
    }

    @Test
    void testGetUsername() {
        assertEquals(TEST_USERNAME, user.getUsername(), "getUsername should return correct username");
    }

    @Test
    void testGetPassword() {
        assertEquals(TEST_PASSWORD, user.getPassword(), "getPassword should return correct password");
    }

    @Test
    void testGetFullName() {
        assertEquals(TEST_FULLNAME, user.getFullName(), "getFullName should return correct full name");
    }

    @Test
    void testGetRole() {
        assertEquals(TEST_ROLE, user.getRole(), "getRole should return correct role");
    }

    @Test
    void testUserWithDifferentRole() {
        User adminUser = new User(2, "admin", "adminpass", "Admin User", "admin");
        assertEquals("admin", adminUser.getRole(), "Role should be admin");
    }

}