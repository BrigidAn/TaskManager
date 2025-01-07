/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ochim
 */
public class TaskController {
  private TaskManagerUI view;
    private List<Task> tasks;

    public TaskController(TaskManagerUI view) {
        this.view = view;
        this.tasks = new ArrayList<>();
    }

    public void saveTask(Task task) {
        tasks.add(task);
        // In a real app, you'd save this to a database instead of the list.
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        // Also delete from the database here.
    }

    public List<Task> getAllTasks() {
        return tasks;
        // Retrieve tasks from the database in a real app.
    }

    public Task getTaskAt(int index) {
        return tasks.get(index);
        // Retrieve the task from the database in a real app.
    }

    public void updateTask(Task task) {
        // In a real app, update the task in the database.
    }
}