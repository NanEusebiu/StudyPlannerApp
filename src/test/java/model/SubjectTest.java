package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {
    private Subject subject;
    private final int TEST_ID = 1;
    private final String TEST_NAME = "Test Subject";
    private final int TEST_USER_ID = 1;

    @BeforeEach
    void setUp() {
        subject = new Subject(TEST_ID, TEST_NAME, TEST_USER_ID);
    }

    @Test
    void testSubjectConstructor() {
        assertNotNull(subject, "Subject object should be created");
        assertEquals(TEST_ID, subject.getId(), "ID should match constructor argument");
        assertEquals(TEST_NAME, subject.getName(), "Name should match constructor argument");
        assertEquals(TEST_USER_ID, subject.getUserId(), "User ID should match constructor argument");
    }

    @Test
    void testGetId() {
        assertEquals(TEST_ID, subject.getId(), "getId should return correct ID");
    }

    @Test
    void testGetName() {
        assertEquals(TEST_NAME, subject.getName(), "getName should return correct name");
    }

    @Test
    void testGetUserId() {
        assertEquals(TEST_USER_ID, subject.getUserId(), "getUserId should return correct user ID");
    }

    @Test
    void testToString() {
        assertEquals(TEST_NAME, subject.toString(), "toString should return the subject name");
    }

    @Test
    void testSubjectEquality() {
        Subject sameSubject = new Subject(TEST_ID, TEST_NAME, TEST_USER_ID);
        Subject differentSubject = new Subject(2, "Different Subject", TEST_USER_ID);

        assertEquals(subject, sameSubject, "Subjects with same ID should be equal");
        assertNotEquals(subject, differentSubject, "Subjects with different IDs should not be equal");
    }

    @Test
    void testHashCode() {
        Subject sameSubject = new Subject(TEST_ID, TEST_NAME, TEST_USER_ID);
        assertEquals(subject.hashCode(), sameSubject.hashCode(),
                "Hash codes should be equal for subjects with same ID");
    }
}