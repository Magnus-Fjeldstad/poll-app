package com.poll.pollapp.jpa.polls;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User voter;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private VoteOption votesOn;

    public Vote() {
    }

    public Vote(User voter, VoteOption votesOn) {
        this.voter = voter;
        this.votesOn = votesOn;
    }
}
