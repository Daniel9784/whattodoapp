package com.whattodo.whattodoapp.service;

import com.whattodo.whattodoapp.dto.NoteRequest;
import com.whattodo.whattodoapp.dto.ShowNotesRequest;
import com.whattodo.whattodoapp.model.Note.Note;
import com.whattodo.whattodoapp.model.Note.NoteRepository;
import com.whattodo.whattodoapp.model.User.User;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class NoteService {

    private final NoteRepository noteRepository;


    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note addNote(CustomUserDetails userDetails, NoteRequest noteRequest) {
        User user = userDetails.getUser();

        Note note = new Note();
        note.setCategory(noteRequest.getCategory());
        note.setContent(noteRequest.getContent());
        note.setUser(user);

        if (noteRequest.getDueDate() != null && !noteRequest.getDueDate().isEmpty()) {
            note.setDueDate(LocalDate.parse(noteRequest.getDueDate()));
        }
        if (noteRequest.getDueTime() != null && !noteRequest.getDueTime().isEmpty()) {
            note.setDueTime(LocalTime.parse(noteRequest.getDueTime()));
        }

        return noteRepository.save(note);
    }

    public List<ShowNotesRequest> getNotes(CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<Note> notes = noteRepository.findByUserId(user.getId());

        return notes.stream().map(note -> {
            ShowNotesRequest dto = new ShowNotesRequest();
            dto.setId(note.getId());
            dto.setCategory(note.getCategory());
            dto.setContent(note.getContent());
            dto.setCreatedAt(note.getCreatedAt() != null ? note.getCreatedAt().toString() : "unknown");
            dto.setDueDate(note.getDueDate() != null ? note.getDueDate().toString() : null);
            dto.setDueTime(note.getDueTime() != null ? note.getDueTime().toString() : null);
            return dto;
        }).collect(Collectors.toList());

    }

}

