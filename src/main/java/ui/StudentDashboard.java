package ui;

import model.Task;
import model.Subject;
import service.TaskService;
import service.SubjectService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.io.*;

public class StudentDashboard extends JFrame {
    private final int userId;
    private final DefaultTableModel taskModel;
    private final JTable tasksTable;
    private final TaskService taskService;
    private final SubjectService subjectService;
    private final JComboBox<Subject> subjectFilter;

    public StudentDashboard(int userId) {
        this.userId = userId;
        this.taskService = new TaskService();
        this.subjectService = new SubjectService();

        setTitle("Student Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addTaskBtn = new JButton("Add Task");
        JButton editTaskBtn = new JButton("Edit Task");
        JButton deleteTaskBtn = new JButton("Delete Task");
        JButton addSubjectBtn = new JButton("Add Subject");
        JButton exportBtn = new JButton("Export Tasks");
        JButton logoutBtn = new JButton("Logout");

        controlPanel.add(addTaskBtn);
        controlPanel.add(editTaskBtn);
        controlPanel.add(deleteTaskBtn);
        controlPanel.add(addSubjectBtn);
        controlPanel.add(exportBtn);
        controlPanel.add(logoutBtn);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectFilter = new JComboBox<>();
        JButton filterBtn = new JButton("Apply Filter");
        filterPanel.add(new JLabel("Filter by Subject:"));
        filterPanel.add(subjectFilter);
        filterPanel.add(filterBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        taskModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Description", "Subject", "Deadline", "Status"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tasksTable = new JTable(taskModel);
        mainPanel.add(new JScrollPane(tasksTable), BorderLayout.CENTER);


        addTaskBtn.addActionListener(e -> showAddTaskDialog());
        editTaskBtn.addActionListener(e -> editSelectedTask());
        deleteTaskBtn.addActionListener(e -> deleteSelectedTask());
        addSubjectBtn.addActionListener(e -> showAddSubjectDialog());
        exportBtn.addActionListener(e -> exportTasks());
        logoutBtn.addActionListener(e -> logout());
        filterBtn.addActionListener(e -> refreshTaskList());

        add(mainPanel);


        refreshSubjects();
        refreshTaskList();
        checkDueTasks();

        setVisible(true);
    }

    private void refreshSubjects() {
        subjectFilter.removeAllItems();
        subjectFilter.addItem(new Subject(0, "All Subjects", userId));
        for (Subject subject : subjectService.getSubjectsByUser(userId)) {
            subjectFilter.addItem(subject);
        }
    }

    private void showAddTaskDialog() {
        JDialog dialog = new JDialog(this, "Add New Task", true);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));
        dialog.setSize(400, 300);

        JTextField titleField = new JTextField();
        JTextArea descField = new JTextArea();
        JTextField deadlineField = new JTextField();
        JComboBox<Subject> subjectBox = new JComboBox<>();


        for (Subject subject : subjectService.getSubjectsByUser(userId)) {
            subjectBox.addItem(subject);
        }

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Description:"));
        dialog.add(new JScrollPane(descField));
        dialog.add(new JLabel("Subject:"));
        dialog.add(subjectBox);
        dialog.add(new JLabel("Deadline (YYYY-MM-DD):"));
        dialog.add(deadlineField);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                Subject selected = (Subject) subjectBox.getSelectedItem();
                Task task = new Task(
                        0,
                        titleField.getText(),
                        descField.getText(),
                        LocalDate.parse(deadlineField.getText()),
                        "Pending",
                        selected.getId(),
                        userId
                );

                if (taskService.addTask(task)) {
                    refreshTaskList();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(saveBtn);
        dialog.add(cancelBtn);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editSelectedTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a task to edit");
            return;
        }

    }

    private void deleteSelectedTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete");
            return;
        }

        int taskId = (int) taskModel.getValueAt(selectedRow, 0);
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this task?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (taskService.deleteTask(taskId)) {
                refreshTaskList();
            }
        }
    }

    private void showAddSubjectDialog() {
        String subjectName = JOptionPane.showInputDialog(this, "Enter subject name:");
        if (subjectName != null && !subjectName.trim().isEmpty()) {
            if (subjectService.addSubject(subjectName, userId)) {
                refreshSubjects();
            }
        }
    }

    private void refreshTaskList() {
        taskModel.setRowCount(0);
        Subject selectedSubject = (Subject) subjectFilter.getSelectedItem();

        List<Task> tasks = taskService.getTasksByUser(userId);
        for (Task task : tasks) {
            if (selectedSubject == null || selectedSubject.getId() == 0 ||
                    task.getSubjectId() == selectedSubject.getId()) {
                taskModel.addRow(new Object[]{
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getSubjectId(),
                        task.getDeadline(),
                        task.getStatus()
                });
            }
        }
    }

    private void checkDueTasks() {
        List<Task> dueTasks = taskService.getTasksDueToday(userId);
        if (!dueTasks.isEmpty()) {
            StringBuilder message = new StringBuilder("Tasks due today:\n");
            for (Task task : dueTasks) {
                message.append("- ").append(task.getTitle()).append("\n");
            }
            JOptionPane.showMessageDialog(this, message.toString(),
                    "Due Tasks", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportTasks() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("tasks_export.txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                List<Task> tasks = taskService.getTasksByUser(userId);
                for (Task task : tasks) {
                    writer.println("Task: " + task.getTitle());
                    writer.println("Description: " + task.getDescription());
                    writer.println("Deadline: " + task.getDeadline());
                    writer.println("Status: " + task.getStatus());
                    writer.println("-------------------");
                }
                JOptionPane.showMessageDialog(this, "Tasks exported successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting tasks: " + e.getMessage());
            }
        }
    }

    private void logout() {
        dispose();
        new LoginForm();
    }
}