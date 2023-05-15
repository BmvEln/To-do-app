package ru.bikmaev.its.dao;

import org.springframework.stereotype.Component;
import ru.bikmaev.its.models.Note;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteDAO {

    private static int NOTE_COUNT;
    private static List<Note> notes;

    {
        notes = new ArrayList<>();

        notes.add(new Note(++NOTE_COUNT, "My note", "Jingle Bells."));
        notes.add(new Note(++NOTE_COUNT, "Business", "1. Food"));
        notes.add(new Note(++NOTE_COUNT, "What to Buy?", "1. Toilet Paper"));
        notes.add(new Note(++NOTE_COUNT, "Hobbies", "1. Do your program"));
    }

    // Посмотреть все заметки
    public List<Note> index() {
        return notes;
    }

    // Посмотреть конкретную заметку
    public Note show(int id) {
        return notes.stream().filter(note -> note.getId() == id).findAny().orElse(null);
    }

    // Сохранить заметку
    public void save(Note note) {
        note.setId(++NOTE_COUNT);
        notes.add(note);
    }

    // Обновить заметку
    public void update(int id, Note updatedNote) {
        Note noteToBeUpdated = show(id);
        noteToBeUpdated.setTitle(updatedNote.getTitle());
        noteToBeUpdated.setDescription(updatedNote.getDescription());
    }

    // Удалить заметку
    public void delete(int id) {
        notes.removeIf(b -> b.getId() == id);
    }
}


