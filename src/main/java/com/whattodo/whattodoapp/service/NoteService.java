package com.whattodo.whattodoapp.service;

import com.whattodo.whattodoapp.dto.NoteRequest;
import com.whattodo.whattodoapp.model.Note.Note;
import com.whattodo.whattodoapp.model.Note.NoteRepository;
import com.whattodo.whattodoapp.model.User.User;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return noteRepository.save(note);
    }

    public List<Note> getNotes(CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return noteRepository.findByUserId(user.getId());
    }
}

