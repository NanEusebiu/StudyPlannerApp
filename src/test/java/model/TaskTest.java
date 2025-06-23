package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;
    private final int TEST_ID = 1;
    private final String TEST_TITLE = "Test Task";
    private final String TEST_DESCRIPTION = "Test Description";
    private final LocalDate TEST_DEADLINE = LocalDate.now();
    private final String TEST_STATUS = "Pending";
    private final int TEST_SUBJECT_ID = 1;
    private final int TEST_USER_ID = 1;

    @BeforeEach
    void setUp() {
        task = new Task(TEST_ID, TEST_TITLE, TEST_DESCRIPTION, TEST_DEADLINE,
                TEST_STATUS, TEST_SUBJECT_ID, TEST_USER_ID);
    }

    @Test
    void testTaskConstructor() {
        assertNotNull(task, "Task object should be created");
        assertEquals(TEST_ID, task.getId(), "ID should match constructor argument");
        assertEquals(TEST_TITLE, task.getTitle(), "Title should match constructor argument");
        assertEquals(TEST_DESCRIPTION, task.getDescription(), "Description should match constructor argument");
        assertEquals(TEST_DEADLINE, task.getDeadline(), "Deadline should match constructor argument");
        assertEquals(TEST_STATUS, task.getStatus(), "Status should match constructor argument");
        assertEquals(TEST_SUBJECT_ID, task.getSubjectId(), "Subject ID should match constructor argument");
        assertEquals(TEST_USER_ID, task.getUserId(), "User ID should match constructor argument");
    }

    @Test
    void testGetId() {
        assertEquals(TEST_ID, task.getId(), "getId should return correct ID");
    }

    @Test
    void testGetTitle() {
        assertEquals(TEST_TITLE, task.getTitle(), "getTitle should return correct title");
    }

    @Test
    void testGetDescription() {
        assertEquals(TEST_DESCRIPTION, task.getDescription(), "getDescription should return correct description");
    }

    @Test
    void testGetDeadline() {
        assertEquals(TEST_DEADLINE, task.getDeadline(), "getDeadline should return correct deadline");
    }

    @Test
    void testGetStatus() {
        assertEquals(TEST_STATUS, task.getStatus(), "getStatus should return correct status");
    }

    @Test
    void testGetSubjectId() {
        assertEquals(TEST_SUBJECT_ID, task.getSubjectId(), "getSubjectId should return correct subject ID");
    }

    @Test
    void testGetUserId() {
        assertEquals(TEST_USER_ID, task.getUserId(), "getUserId should return correct user ID");
    }

    @Test
    void testSetStatus() {
        String newStatus = "Completed";
        task.setStatus(newStatus);
        assertEquals(newStatus, task.getStatus(), "setStatus should update the status");
    }

    @Test
    void testTaskWithPastDeadline() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Task pastTask = new Task(2, "Past Task", "Description", pastDate, "Pending", 1, 1);
        assertTrue(pastTask.getDeadline().isBefore(LocalDate.now()), "Task deadline should be in the past");
    }

    @Test
    void testTaskWithFutureDeadline() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Task futureTask = new Task(3, "Future Task", "Description", futureDate, "Pending", 1, 1);
        assertTrue(futureTask.getDeadline().isAfter(LocalDate.now()), "Task deadline should be in the future");
    }
}