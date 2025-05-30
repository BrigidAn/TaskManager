
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author ochim
 */
public class TaskStorage {
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
        return new ArrayList<>(); // file doesn't exist yet
    } catch (IOException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
  }
}
