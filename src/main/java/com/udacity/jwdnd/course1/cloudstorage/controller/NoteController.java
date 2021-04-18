package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class NoteController {
    private final UserService userService;
    private final NoteListService noteListService;

    public NoteController(UserService userService, NoteListService noteListService) {
        this.userService = userService;
        this.noteListService = noteListService;
    }

    @PostMapping("/notes")
    public RedirectView createOrUpdateNote(Authentication authentication, Note noteForm, RedirectAttributes redirectAttributes) {
        int userId = userService.getUserId(authentication.getName());
        noteForm.setUserId(userId);
        if (noteForm.getNoteId() == null) {
            this.noteListService.addNote(noteForm);
        } else {
            this.noteListService.updateNote(noteForm);
        }
        noteForm.setNoteId(null);
        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        return new RedirectView("home");
    }

    @GetMapping(value = "/notes/delete/{noteTitle}")
    public RedirectView deleteNote(Authentication authentication, @PathVariable String noteTitle, RedirectAttributes redirectAttributes) {
        int userId = userService.getUserId(authentication.getName());
        boolean wasNoteDeleted = noteListService.deleteNote(userId, noteTitle) > 0;
        if (wasNoteDeleted) {
            redirectAttributes.addFlashAttribute("noteSuccess", "Note successfully deleted.");
        } else {
            redirectAttributes.addFlashAttribute("noteError", "Note couldn't be deleted.");
        }
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        return new RedirectView("/home");
    }
}
