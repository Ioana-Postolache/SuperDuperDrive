package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
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

    // for download note
//    @GetMapping("/notes/{noteName}")
//    public @ResponseBody
//    ResponseEntity<byte[]> getNote(Authentication authentication, @PathVariable String noteName) {
//        int userId = userService.getUser(authentication.getName()).getUserId();
//        Note note = this.noteListService.getNote(userId, noteName);
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(note.getContentType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; notename=\"" + noteName + "\"")
//                .body(note.getNoteData());
//    }
//    // option using param
//    @GetMapping("/notes")
//    public @ResponseBody
//    byte[] getNote(Authentication authentication, @RequestParam("noteName") String noteName) {
//        int userId = userService.getUser(authentication.getName()).getUserId();
//        Note note = this.noteListService.getNote(userId, noteName);
//        return note.getNoteData();
//    }

    @PostMapping("/notes")
    public RedirectView createNote(Authentication authentication, NoteForm noteForm, RedirectAttributes redirectAttributes) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        noteForm.setUserId(userId);
        this.noteListService.addNote(noteForm);
        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        return new RedirectView("home");
    }

    @GetMapping(value = "/notes/delete/{noteTitle}")
    public RedirectView deleteNote(Authentication authentication, @PathVariable String noteTitle, RedirectAttributes redirectAttributes) {
        int userId = userService.getUser(authentication.getName()).getUserId();
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
