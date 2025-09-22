package com.poll.pollapp.jpa.polls;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<VoteOption> options = new ArrayList<>();

    public Poll() {
    }

    public Poll(String question, User createdBy) {
        this.question = question;
        this.createdBy = createdBy;
    }

    public VoteOption addVoteOption(String caption) {
        int order = options.size();
        VoteOption option = new VoteOption(caption, order, this);
        options.add(option);
        return option;
    }
}
