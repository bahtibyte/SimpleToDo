package com.bahti.simpletodo.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Task implements Comparable<Task>{

    public static ArrayList<Task> tasks;

    private String name;
    private String notes;
    private int reminder;
    private int priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString(){
        return name+","+reminder+","+priority+","+notes;
    }

    public String display() { return priority +"% "+name; }

    public int compareTo(Task task) {
        if (priority < task.getPriority()){
            return 1;
        }else if (priority > task.getPriority()){
            return -1;
        }else{
            return name.compareTo(task.getName());
        }
    }
}
