package com.example.booksocial_backend.domain.social;

import java.time.LocalDateTime;

import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AUTHOR_FOLLOW", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "author_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(nullable = false)
    private LocalDateTime followDate;
}
