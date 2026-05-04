package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booksocial_backend.domain.social.AuthorFollow;

@Repository
public interface AuthorFollowRepository extends JpaRepository<AuthorFollow, Long> {

    boolean existsByUserIdAndAuthorId(Long userId, Long authorId);

    Optional<AuthorFollow> findByUserIdAndAuthorId(Long userId, Long authorId);

    List<AuthorFollow> findByAuthorId(Long authorId);

    long countByAuthorId(Long authorId);

    @Modifying
    @Query("DELETE FROM AuthorFollow af WHERE af.user.id = :userId AND af.author.id = :authorId")
    void deleteByUserIdAndAuthorId(Long userId, Long authorId);
}
