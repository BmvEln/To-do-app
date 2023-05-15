package ru.bikmaev.its.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bikmaev.its.dao.NoteDAO;
import ru.bikmaev.its.models.Note;

import javax.validation.Valid;

/**
 * @author Elnar_Bikmaev
 *
 */
@Controller
@RequestMapping("/notepad")
public class NotepadController {

    private final NoteDAO noteDAO;

    @Autowired
    public NotepadController(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    @GetMapping("/note")
    public String test(Model model) {
        model.addAttribute("notepad", noteDAO.index());
        return "notepad/note";
    }


    // Получим все заметки по id из DAO и передадим наотображение в представление
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("notepad", noteDAO.index());
        return "notepad/index";
    }

    // Получим одну заметку по id из DAO и передадим на отображение в представление
    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") int id, Model model) {
        model.addAttribute("note", noteDAO.show(id));
        return "notepad/show";
    }

    @GetMapping("/new")
    public String newNote(@ModelAttribute("note") Note note) {
        return "notepad/new";
    }

    @PostMapping()
    public String createNote(@ModelAttribute("note") @Valid Note note,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "notepad/new";

        noteDAO.save(note);
        return "redirect:/notepad";
    }

    @GetMapping("/{id}/edit")
    public String editNote(Model model, @PathVariable("id") int id) {
        model.addAttribute("note", noteDAO.show(id));
        return "notepad/edit";
    }

    // С помощью @ModelAttribute мы должны принимать объект note из формы, а @PathVariable ИД
    @PatchMapping("/{id}")
    public String updateNote(@ModelAttribute("note") @Valid Note note, BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "notepad/edit";

        // Мы должны найти заметку из базы данных с таким ИД и поменять его значение на те значения, которые пришли из формы,
        // то есть на те значения, которые лежат в объекте класса Note, который мы получили с помощью нотации @ModelAttribute
        noteDAO.update(id, note);
        return "redirect:/notepad/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") int id) {
        noteDAO.delete(id);
        return "redirect:/notepad";
    }
}
