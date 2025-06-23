
package ui;

import model.User;
import model.Task;
import model.Subject;
import service.UserService;
import service.TaskService;
import service.SubjectService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final DefaultTableModel userModel;
    private final DefaultTableModel subjectStatsModel;
    private final UserService userService;
    private final TaskService taskService;
    private final SubjectService subjectService;
    private final JTable usersTable;

    public AdminDashboard() {
        this.userService = new UserService();
        this.taskService = new TaskService();
        this.subjectService = new SubjectService();

        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh Data");
        JButton logoutBtn = new JButton("Logout");

        controlPanel.add(refreshBtn);
        controlPanel.add(logoutBtn);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);


        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setBorder(BorderFactory.createTitledBorder("Users"));

        userModel = new DefaultTableModel(
                new String[]{"ID", "Username", "Full Name", "Role", "Tasks Count"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(userModel);
        usersPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);


        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Subject Statistics"));

        subjectStatsModel = new DefaultTableModel(
                new String[]{"Subject", "Total Users", "Total Tasks", "Completion Rate"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable statsTable = new JTable(subjectStatsModel);
        statsPanel.add(new JScrollPane(statsTable), BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(usersPanel);
        splitPane.setRightComponent(statsPanel);
        splitPane.setDividerLocation(500);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refreshData());
        logoutBtn.addActionListener(e -> logout());

        add(mainPanel);

        refreshData();

        setVisible(true);
    }

    private void refreshData() {
        refreshUserTable();
        refreshStatistics();
    }

    private void refreshUserTable() {
        userModel.setRowCount(0);
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            if (!"admin".equalsIgnoreCase(user.getRole())) {  // Don't show admin users
                int taskCount = taskService.getTasksByUser(user.getId()).size();
                userModel.addRow(new Object[]{
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getRole(),
                        taskCount
                });
            }
        }
    }

    private void refreshStatistics() {
        subjectStatsModel.setRowCount(0);
        List<User> students = userService.getAllUsers();

        for (User student : students) {
            if ("student".equalsIgnoreCase(student.getRole())) {
                List<Subject> subjects = subjectService.getSubjectsByUser(student.getId());
                for (Subject subject : subjects) {
                    int totalTasks = 0;
                    int completedTasks = 0;
                    List<Task> tasks = taskService.getTasksByUser(student.getId());

                    for (Task task : tasks) {
                        if (task.getSubjectId() == subject.getId()) {
                            totalTasks++;
                            if ("Completed".equalsIgnoreCase(task.getStatus())) {
                                completedTasks++;
                            }
                        }
                    }

                    double completionRate = totalTasks > 0 ?
                            (completedTasks * 100.0) / totalTasks : 0;

                    subjectStatsModel.addRow(new Object[]{
                            subject.getName(),
                            1,
                            totalTasks,
                            String.format("%.1f%%", completionRate)
                    });
                }
            }
        }
    }

    private void logout() {
        dispose();
        new LoginForm();
    }
}