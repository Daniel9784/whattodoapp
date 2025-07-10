package com.whattodo.whattodoapp.model.Note;

import com.whattodo.whattodoapp.model.User.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @Lob
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")

    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
