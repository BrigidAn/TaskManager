/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package taskmanager;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.stream.Collectors;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author ochim
 */
public class TaskManagerUI extends javax.swing.JFrame {

    private JTable taskTable;
    private JButton addTaskButton, editTaskButton, deleteTaskButton, markAsCompletedButton;
    private JTextField titleField, dueDateField, searchField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox, filterComboBox;
    private JCheckBox recurringCheckBox;
    private JButton saveButton, cancelButton;

    private TaskController controller;
    private int editingRow = -1;

    public TaskManagerUI() throws UnsupportedLookAndFeelException {
        
        
        UIManager.setLookAndFeel(new FlatLightLaf());
        
      controller = new TaskController(this);

        setTitle("Enhanced Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel taskListLabel = new JLabel("Tasks:");
        taskListLabel.setBounds(20, 10, 100, 30);
        add(taskListLabel);

        searchField = new JTextField();
        searchField.setBounds(120, 10, 200, 30);
        add(searchField);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshTaskList();
            }
        });

        filterComboBox = new JComboBox<>(new String[]{"All", "Completed", "Pending"});
        filterComboBox.setBounds(340, 10, 150, 30);
        add(filterComboBox);
        filterComboBox.addActionListener(e -> refreshTaskList());

        taskTable = new JTable(new DefaultTableModel(new Object[]{"Title", "Due Date", "Priority", "Recurring", "Status"}, 0));
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(20, 50, 840, 200);
        add(scrollPane);

        addTaskButton = new JButton("Add Task");
        addTaskButton.setBounds(20, 260, 120, 30);
        add(addTaskButton);

        editTaskButton = new JButton("Edit Task");
        editTaskButton.setBounds(160, 260, 120, 30);
        add(editTaskButton);

        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setBounds(300, 260, 120, 30);
        add(deleteTaskButton);

        markAsCompletedButton = new JButton("Mark as Completed");
        markAsCompletedButton.setBounds(440, 260, 180, 30);
        add(markAsCompletedButton);

        titleField = createLabeledField("Title:", 20, 310, 120, 200);
        descriptionArea = new JTextArea();
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBounds(120, 360, 200, 80);
        add(new JLabel("Description:")).setBounds(20, 360, 100, 30);
        add(descScrollPane);

        priorityComboBox = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});
        add(new JLabel("Priority:")).setBounds(20, 460, 100, 30);
        priorityComboBox.setBounds(120, 460, 200, 30);
        add(priorityComboBox);

        dueDateField = createLabeledField("Due Date (yyyy-MM-dd):", 350, 310, 530, 200);
        recurringCheckBox = new JCheckBox();
        add(new JLabel("Recurring:")).setBounds(350, 360, 100, 30);
        recurringCheckBox.setBounds(530, 360, 200, 30);
        add(recurringCheckBox);

        saveButton = new JButton("Save Task");
        saveButton.setBounds(350, 460, 120, 30);
        add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(500, 460, 120, 30);
        add(cancelButton);

        // Listeners
        addTaskButton.addActionListener(e -> openAddTaskForm());
        editTaskButton.addActionListener(e -> openEditTaskForm());
        deleteTaskButton.addActionListener(e -> deleteTask());
        markAsCompletedButton.addActionListener(e -> markTaskAsCompleted());
        saveButton.addActionListener(e -> saveTask());
        cancelButton.addActionListener(e -> clearForm());

        refreshTaskList();
        setVisible(true);
    }

      private JTextField createLabeledField(String label, int x1, int y1, int x2, int width) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(x1, y1, 180, 30);
        JTextField field = new JTextField();
        field.setBounds(x2, y1, width, 30);
        add(jLabel);
        add(field);
        return field;
    }
    
    private void openAddTaskForm() {
        editingRow = -1;
        clearForm();
    }

    private void openEditTaskForm() {
      editingRow = taskTable.getSelectedRow();
        if (editingRow != -1) {
            Task task = controller.getTaskAt(editingRow);
            titleField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            dueDateField.setText(task.getDueDate().toString());
            priorityComboBox.setSelectedItem(task.getPriority().toString());
            recurringCheckBox.setSelected(task.isRecurring());
        }
    }

    private void deleteTask() {
         int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            Task task = controller.getTaskAt(selectedRow);
            controller.deleteTask(task);
            refreshTaskList();
        }
    }

  private void markTaskAsCompleted() {
    int selectedRow = taskTable.getSelectedRow();
    if (selectedRow != -1) {
        Task task = controller.getTaskAt(selectedRow);
        task.setStatus(Task.Status.COMPLETED); // ✅ set status instead of setCompleted
        controller.updateTask(task);
        refreshTaskList();
    }
}


      private void saveTask() {
        try {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String dueDateStr = dueDateField.getText().trim();
            String priorityStr = (String) priorityComboBox.getSelectedItem();
            boolean isRecurring = recurringCheckBox.isSelected();

            if (title.isEmpty() || dueDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Due Date are required.");
                return;
            }

            LocalDate dueDate = LocalDate.parse(dueDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Task.Priority priority = Task.Priority.valueOf(priorityStr.toUpperCase());

            Task task = new Task(title, description, priority, dueDate, isRecurring);
            task.setStatus(Task.Status.PENDING);

            if (editingRow == -1) {
                controller.saveTask(task);
            } else {
                controller.updateTaskAt(editingRow, task);
                editingRow = -1;
            }

            refreshTaskList();
            clearForm();
            JOptionPane.showMessageDialog(this, "Task saved successfully!");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void refreshTaskList() {
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        model.setRowCount(0);

        String search = searchField.getText().toLowerCase();
        String filter = (String) filterComboBox.getSelectedItem();

        List<Task> filteredTasks = controller.getAllTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(search))
                .filter(task -> {
                    if (filter.equals("All")) return true;
                    if (filter.equals("Completed")) return task.getStatus() == Task.Status.COMPLETED;
                    return task.getStatus() == Task.Status.PENDING;
                }).collect(Collectors.toList());

        for (Task task : filteredTasks) {
            model.addRow(new Object[]{
                    task.getTitle(),
                    task.getDueDate(),
                    task.getPriority(),
                    task.isRecurring() ? "Yes" : "No",
                    task.getStatus()
            });
        }
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        dueDateField.setText("");
        priorityComboBox.setSelectedIndex(0);
        recurringCheckBox.setSelected(false);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception ex) {
        ex.printStackTrace();
    }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TaskManagerUI().setVisible(true);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(TaskManagerUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
