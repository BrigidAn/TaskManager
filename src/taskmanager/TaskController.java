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
    private List<Task> taskList;

    public TaskController(TaskManagerUI view) {
        this.view = view;
        this.taskList = TaskStorage.loadTasks();  
    }
    
    public int getNextTaskId() {
    return taskList.size() + 1; // Simple unique ID generator
}
    
    public void addTask(Task task) {
           taskList.add(task);
           TaskStorage.saveTasks(taskList);
           System.out.println("Task added: " + task.getTitle());
   }
    
    public void saveTask(Task task) {
        taskList.add(task);
        TaskStorage.saveTasks(taskList);
        // In a real app, you'd save this to a database instead of the list.
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
        TaskStorage.saveTasks(taskList);
        System.out.println("Task deleted: " + task.getTitle());
    }

     public Task getTaskAt(int index) {
        if (index >= 0 && index < taskList.size()) {
            return taskList.get(index);
        }
        return null;
    }


    public void updateTaskAt(int index, Task updatedTask) {
        if (index >= 0 && index < taskList.size()) {
            taskList.set(index, updatedTask);
            TaskStorage.saveTasks(taskList);
            System.out.println("Task updated at index " + index);
        }
    }
    
    public List<Task> getAllTasks() {
        return taskList;
    }

    public void updateTask(Task task) {
        // Save entire list after external modification
        TaskStorage.saveTasks(taskList);
    }

    
   
    public void toggleTaskCompletion(Task task) {
        if (task.getStatus() == Task.Status.PENDING) {
            task.setStatus(Task.Status.COMPLETED);
        } else {
            task.setStatus(Task.Status.PENDING);
        }
        TaskStorage.saveTasks(taskList);
        System.out.println("Toggled completion: " + task.getTitle());
    }
}