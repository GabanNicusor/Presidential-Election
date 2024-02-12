package com.presidential.elections.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VoteTable")
public class UserVotes{
    @Id
    @Column(name = "vote_id")
    private Integer voteid;
    private Integer votedPerson;
    private String date;

    public UserVotes(Integer user_id) {
        this.voteid = user_id;
    }

    public UserVotes() {
        super();
    }

    public UserVotes(Integer vote_id, Integer voted_Person, String Date) {
        this.voteid = vote_id;
        this.votedPerson = voted_Person;
        this.date = Date;
    }

    public Integer getVote_id() {
        return voteid;
    }

    public void setVote_id(Integer vote_id) {
        this.voteid = vote_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getVotedPerson() {
        return votedPerson;
    }

    public void setVotedPerson(Integer votedPerson) {
        this.votedPerson = votedPerson;
    }
}
