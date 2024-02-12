package com.presidential.elections.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.presidential.elections.Entities.User;
import com.presidential.elections.Entities.UserCandidate;

public interface CandidateRepository extends JpaRepository<UserCandidate, Integer> {
    @Query("SELECT u FROM User u JOIN UserCandidate c ON u.userid = c.candidateId")
    List<User> findUsersWithCandidates();
}
