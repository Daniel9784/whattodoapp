package com.whattodo.whattodoapp.controller;

import com.whattodo.whattodoapp.dto.NoteRequest;
import com.whattodo.whattodoapp.model.Note.Note;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import com.whattodo.whattodoapp.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody NoteRequest noteRequest,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Note savedNote = noteService.addNote(userDetails, noteRequest);
        return ResponseEntity.ok("Note added with ID: " + savedNote.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Note> getNotes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return noteService.getNotes(userDetails);
    }
}
