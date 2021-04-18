package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteListService {
    private final NoteMapper noteMapper;

    public NoteListService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(Note note) {
        noteMapper.insert(note);
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getAllNotesByUser(userId);
    }

    public int deleteNote(int userId, String noteTitle) {
        return noteMapper.deleteNoteByNoteName(userId, noteTitle);
    }

    public int updateNote(Note note) {
        return noteMapper.update(note);
    }
}
