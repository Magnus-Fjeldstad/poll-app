package com.poll.pollapp.jpa.polls;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Poll> created = new LinkedHashSet<>();

    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Vote> votes = new LinkedHashSet<>();

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Poll createPoll(String question) {
        Poll poll = new Poll(question, this);
        created.add(poll);
        return poll;
    }

    public Vote voteFor(VoteOption option) {
        Vote vote = new Vote(this, option);
        votes.add(vote);
        option.getVotes().add(vote);
        return vote;
    }
}
