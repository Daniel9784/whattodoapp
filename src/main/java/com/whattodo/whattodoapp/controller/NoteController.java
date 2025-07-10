package com.whattodo.whattodoapp.controller;

import com.whattodo.whattodoapp.dto.NoteRequest;
import com.whattodo.whattodoapp.dto.ShowNotesRequest;
import com.whattodo.whattodoapp.model.Note.Note;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import com.whattodo.whattodoapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/user/add-note")
    public ResponseEntity<?> addNote(@Valid @RequestBody NoteRequest noteRequest,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Note savedNote = noteService.addNote(userDetails, noteRequest);
        return ResponseEntity.ok("Note added with ID: " + savedNote.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/user/show-notes")
    public List<ShowNotesRequest> getNotes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return noteService.getNotes(userDetails);
    }
}
