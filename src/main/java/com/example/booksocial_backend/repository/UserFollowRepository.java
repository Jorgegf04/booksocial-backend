package com.example.booksocial_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.domain.user.UserFollow;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

  Optional<UserFollow> findByFollowerAndFollowing(User follower, User following);

  List<UserFollow> findByFollower(User follower);

  List<UserFollow> findByFollowing(User following);

  boolean existsByFollowerAndFollowing(User follower, User following);
}