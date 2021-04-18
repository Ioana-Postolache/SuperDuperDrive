package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteListService {
    private final NoteMapper noteMapper;

    public NoteListService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(NoteForm noteForm) {
        Note newNote = new Note();
        newNote.setNoteTitle(noteForm.getNoteTitle());
        newNote.setNoteDescription(noteForm.getNoteDescription());
        newNote.setUserId(noteForm.getUserId());
        noteMapper.insert(newNote);
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getAllNotesByUser(userId);
    }

    public Note getNote(int userId, String noteTitle) {
        return noteMapper.getNoteByNoteName(userId, noteTitle);
    }

    public boolean doesNoteAlreadyExist(int userId, String noteTitle){
        return getNote(userId, noteTitle) != null;
    }

    public int deleteNote(int userId, String noteTitle) {
        return noteMapper.deleteNoteByNoteName(userId, noteTitle);
    }
}
