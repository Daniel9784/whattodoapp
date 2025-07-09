package com.whattodo.whattodoapp.controller;

import com.whattodo.whattodoapp.dto.NoteRequest;
import com.whattodo.whattodoapp.model.Note.Note;
import com.whattodo.whattodoapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody NoteRequest noteRequest, Principal principal) {
        Note savedNote = noteService.addNote(principal.getName(), noteRequest);
        return ResponseEntity.ok("Note added with ID: " + savedNote.getId());
    }

    @GetMapping
    public List<Note> getNotes(Principal principal) {
        return noteService.getNotes(principal.getName());
    }
}
