/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ochim
 */
public class Task {
    public enum Priority { HIGH, MEDIUM, LOW }
    public enum Status { PENDING, COMPLETED }
    
   private int id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private boolean isRecurring;
    private String dueDate; 

    public Task() {
        // Default constructor needed for Gson
    }

    public Task(String title, String description, Priority priority, LocalDate dueDate, boolean isRecurring) {
    this.id = -1; // or generate a unique ID
    this.title = title;
    this.description = description;
    this.priority = priority;
    this.status = Status.PENDING;
    this.isRecurring = isRecurring;
    this.dueDate = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
}


    // Getters and setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}
    
   