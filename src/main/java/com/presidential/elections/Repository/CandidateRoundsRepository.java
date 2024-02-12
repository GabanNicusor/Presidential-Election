package com.presidential.elections.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.presidential.elections.Entities.CandidateRounds;

public interface CandidateRoundsRepository extends JpaRepository<CandidateRounds, Integer> {
    @Query("SELECT max(u.id) FROM CandidateRounds u")
    Integer findMaxId();
}
