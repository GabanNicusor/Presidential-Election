package com.presidential.elections.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rounds_date")
public class CandidateRounds {
    @Id
    @Column(name = "rounds_date_id")
    private Integer rounds_date_id;
    private String start_datetime;
    private String end_datetime;

    public CandidateRounds() {
        super();
    }
    
    public CandidateRounds(Integer roundsid, String startDateTime, String endDateTime) {
        this.rounds_date_id = roundsid;
        this.start_datetime = startDateTime;
        this.end_datetime = endDateTime;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }
    
    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }
    
    
}
