package com.whattodo.whattodoapp.service;

import com.whattodo.whattodoapp.dto.NoteRequest;
import com.whattodo.whattodoapp.model.Note.Note;
import com.whattodo.whattodoapp.model.Note.NoteRepository;
import com.whattodo.whattodoapp.model.User.User;
import com.whattodo.whattodoapp.model.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Note addNote(String username, NoteRequest noteRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setCategory(noteRequest.getCategory());
        note.setContent(noteRequest.getContent());
        note.setUser(user);

        return noteRepository.save(note);
    }

    public List<Note> getNotes(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return noteRepository.findByUserId(user.getId());
    }
}
