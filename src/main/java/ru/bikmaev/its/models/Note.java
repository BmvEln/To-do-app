package ru.bikmaev.its.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Note {

    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 1, max = 255, message = "Name should be between 1 and 255 characters")
    private String title;

    @NotEmpty(message = "Description should not be empty")
    @Size(min = 1, max = 255, message = "Name should be between 1 and 255 characters")
    private String description;

    public Note() {

    }

    public Note(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

}
