/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ochim
 */
public class TaskController {
     private List<Task> taskList;
    private static final String FILE_PATH = "tasks.json";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TaskController(TaskManagerUI view) {
        loadTasks();
    }

    private void loadTasks() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Task>>() {}.getType();
            taskList = gson.fromJson(reader, listType);
            if (taskList == null) taskList = new ArrayList<>();
        } catch (IOException e) {
            taskList = new ArrayList<>();
        }
    }

    public void saveTasks() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(taskList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() { return taskList; }

    public void saveTask(Task task) {
        taskList.add(task);
        saveTasks();
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
        saveTasks();
    }

    public void updateTaskAt(int index, Task task) {
        taskList.set(index, task);
        saveTasks();
    }

    public Task getTaskAt(int row) {
        return taskList.get(row);
    }

    public void updateTask(Task task) {
        // No-op if using index-based updates
        saveTasks();
    }

    private void saveTasksToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            new Gson().toJson(taskList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextTaskId() {
    return taskList.size() + 1; // Simple unique ID generator
}
    
    public void addTask(Task task) throws IOException {
           taskList.add(task);
           TaskStorage.saveTasks(taskList);
           System.out.println("Task added: " + task.getTitle());
   }

     
    private void save() {
        try {
            TaskStorage.saveTasks(taskList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    public void toggleTaskCompletion(Task task) throws IOException {
        if (task.getStatus() == Task.Status.PENDING) {
            task.setStatus(Task.Status.COMPLETED);
        } else {
            task.setStatus(Task.Status.PENDING);
        }
        TaskStorage.saveTasks(taskList);
        System.out.println("Toggled completion: " + task.getTitle());
    }
}