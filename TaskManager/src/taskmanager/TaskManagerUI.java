/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package taskmanager;

import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

/**
 *
 * @author ochim
 */
public class TaskManagerUI extends javax.swing.JFrame {

    private JTable taskTable;
    private JButton addTaskButton, editTaskButton, deleteTaskButton, markAsCompletedButton;
    private JTextField titleField, dueDateField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox;
    private JButton saveButton, cancelButton;
    private TaskController controller;
    
    public TaskManagerUI() {
        initComponents();
         controller = new TaskController(this);
         
         setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // Task List Panel
        JLabel taskListLabel = new JLabel("Tasks:");
        taskListLabel.setBounds(20, 20, 100, 30);
        add(taskListLabel);

        taskTable = new JTable(new DefaultTableModel(new Object[]{"Title", "Due Date", "Priority", "Status"}, 0));
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(20, 60, 740, 200);
        add(scrollPane);

        addTaskButton = new JButton("Add Task");
        addTaskButton.setBounds(20, 280, 120, 30);
        add(addTaskButton);

        editTaskButton = new JButton("Edit Task");
        editTaskButton.setBounds(160, 280, 120, 30);
        add(editTaskButton);

        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setBounds(300, 280, 120, 30);
        add(deleteTaskButton);

        markAsCompletedButton = new JButton("Mark as Completed");
        markAsCompletedButton.setBounds(440, 280, 160, 30);
        add(markAsCompletedButton);

        // Task Details Panel
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(20, 330, 100, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(120, 330, 200, 30);
        add(titleField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(20, 380, 100, 30);
        add(descriptionLabel);

        descriptionArea = new JTextArea();
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBounds(120, 380, 200, 80);
        add(descScrollPane);

        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setBounds(20, 480, 100, 30);
        add(priorityLabel);

        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityComboBox.setBounds(120, 480, 200, 30);
        add(priorityComboBox);

        JLabel dueDateLabel = new JLabel("Due Date:");
        dueDateLabel.setBounds(350, 330, 100, 30);
        add(dueDateLabel);

        dueDateField = new JTextField();
        dueDateField.setBounds(450, 330, 200, 30);
        add(dueDateField);

        saveButton = new JButton("Save Task");
        saveButton.setBounds(350, 480, 120, 30);
        add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(500, 480, 120, 30);
        add(cancelButton);

         addTaskButton.addActionListener(e -> openAddTaskForm());
        editTaskButton.addActionListener(e -> openEditTaskForm());
        deleteTaskButton.addActionListener(e -> deleteTask());
        markAsCompletedButton.addActionListener(e -> markTaskAsCompleted());
        saveButton.addActionListener(e -> saveTask());
        cancelButton.addActionListener(e -> clearForm());
        
        setVisible(true);
    }

     private void openAddTaskForm() {
        clearForm();
    }

      private void openEditTaskForm() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            Task selectedTask = controller.getTaskAt(selectedRow);
            titleField.setText(selectedTask.getTitle());
            descriptionArea.setText(selectedTask.getDescription());
            dueDateField.setText(selectedTask.getDueDate().toString());
            priorityComboBox.setSelectedItem(selectedTask.getPriority());
        }
    }
      
        private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            Task selectedTask = controller.getTaskAt(selectedRow);
            controller.deleteTask(selectedTask);
            refreshTaskList();
        }
    }
    
     private void markTaskAsCompleted() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            Task selectedTask = controller.getTaskAt(selectedRow);
            selectedTask.setCompleted(true);
            controller.updateTask(selectedTask);
            refreshTaskList();
        }
    }
     
      private void saveTask() {
    try {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String dueDateStr = dueDateField.getText(); // User input: "12/02/25"
        String priority = (String) priorityComboBox.getSelectedItem();

        // Define the expected date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        
        // Parse the date
        LocalDate dueDate = LocalDate.parse(dueDateStr, formatter);

        Task task = new Task(title, description, dueDate, priority, false);
        controller.saveTask(task);
        refreshTaskList();
        clearForm();

        JOptionPane.showMessageDialog(this, "Task saved successfully!");
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Please use MM/dd/yy.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saving task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // Refresh the task list in the table
    private void refreshTaskList() {
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        model.setRowCount(0);  // Clear existing rows
        for (Task task : controller.getAllTasks()) {
            model.addRow(new Object[]{task.getTitle(), task.getDueDate(), task.getPriority(), task.isCompleted() ? "Completed" : "Pending"});
        }
    }

    // Clear form
    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        dueDateField.setText("");
        priorityComboBox.setSelectedIndex(0);
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
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TaskManagerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaskManagerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaskManagerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaskManagerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
            new TaskManagerUI();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TaskManagerUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
