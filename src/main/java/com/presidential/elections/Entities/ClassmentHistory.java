package com.presidential.elections.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rounds_history")
public class ClassmentHistory {
    @Id
    @Column(name = "round_id")
    private Integer round_id;
    private Integer candidate_id;
    private Integer number_votes;

    public ClassmentHistory() {
        super();
    }

    public ClassmentHistory(Integer round_id, Integer candidate_id, Integer number_votes) {
        this.round_id = round_id;
        this.candidate_id = candidate_id;
        this.number_votes  = number_votes;
    }

    public Integer getCandidate_id() {
        return candidate_id;
    }
    
    public void setCandidate_id(Integer candidate_id) {
        this.candidate_id = candidate_id;
    }

    public Integer getNumber_votes() {
        return number_votes;
    }

    public void setNumber_votes(Integer number_votes) {
        this.number_votes = number_votes;
    }

    
}
