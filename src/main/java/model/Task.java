package model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDate deadline;
    private String status;
    private int subjectId;
    private int userId;

    public Task(int id, String title, String description, LocalDate deadline,
                String status, int subjectId, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.subjectId = subjectId;
        this.userId = userId;
    }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public int getSubjectId() { return subjectId; }
    public int getUserId() { return userId; }

    public void setStatus(String status) { this.status = status; }
}