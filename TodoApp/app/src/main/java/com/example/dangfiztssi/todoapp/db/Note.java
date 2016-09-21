package com.example.dangfiztssi.todoapp.db;

/**
 * Created by DangF on 09/19/16.
 */
public class Note {
    private long id ;
    private String title;
    private String description;
    private String color;
    private String dueDate;
    private boolean isDone;
    private boolean isReminder;
    private boolean isPriority;
    private int colorType;

    public Note(){

    }

    public Note(String title, String description, String color, String dueDate, boolean isDone, boolean isReminder, boolean isPriority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isDone = isDone;
        this.isReminder = isReminder;
        this.isPriority = isPriority;
        this.color = color;
//        this.colorType = colorType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }
}
