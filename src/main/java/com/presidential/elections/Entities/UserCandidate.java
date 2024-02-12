package com.presidential.elections.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_candidate")
public class UserCandidate {
    @Id
    @Column(name = "candidate_id")
    private Integer candidateId;

    public UserCandidate() {
        super();
    }

    public UserCandidate(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }
}
