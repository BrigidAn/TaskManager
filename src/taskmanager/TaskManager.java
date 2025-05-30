/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package taskmanager;

import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ochim
 */
public class TaskManager {
    private static final String FILE_PATH = "tasks.json";
    private static final Gson gson = new Gson();

    public static void saveTasks(List<Task> tasks) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Task> loadTasks() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Task[] tasksArray = gson.fromJson(reader, Task[].class);
            if (tasksArray == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(tasksArray));
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public static void main(String[] args) {
     
           try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new TaskManagerUI());
    }
    
}
