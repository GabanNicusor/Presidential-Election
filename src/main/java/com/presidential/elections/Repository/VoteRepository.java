package com.presidential.elections.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presidential.elections.Entities.UserVotes;

public interface VoteRepository extends JpaRepository<UserVotes, Integer> {
    Optional<UserVotes> findByvoteid(Integer vote_id);
}
