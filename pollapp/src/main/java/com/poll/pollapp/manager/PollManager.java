package com.poll.pollapp.manager;

import com.poll.pollapp.domain.Poll;
import com.poll.pollapp.domain.User;
import com.poll.pollapp.domain.Vote;
import com.poll.pollapp.domain.VoteOption;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PollManager {

    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final Map<UUID, Poll> polls = new ConcurrentHashMap<>();
    private final Map<UUID, VoteOption> options = new ConcurrentHashMap<>();
    private final Map<UUID, Vote> votes = new ConcurrentHashMap<>();

    // -------- Users --------
    public List<User> listUsers() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> getUser(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    public User saveUser(User u) {
        users.put(u.getId(), u);
        return u;
    }

    public void deleteUser(UUID id) {
        var u = users.remove(id);
        if (u == null) return;
        // cleanup: remove created polls & votes by this user
        u.getCreated().forEach(p -> deletePoll(p.getId()));
        u.getVoted().forEach(v -> deleteVote(v.getId()));
    }

    // -------- Polls --------
    public List<Poll> listPolls() {
        return new ArrayList<>(polls.values());
    }

    public Optional<Poll> getPoll(UUID id) {
        return Optional.ofNullable(polls.get(id));
    }

    public Poll createPoll(User creator, String question, Instant publishedAt, Instant validUntil, List<VoteOption> opts) {
        Poll p = Poll.builder()
                .question(question)
                .publishedAt(publishedAt)
                .validUntil(validUntil)
                .createdBy(creator)
                .build();

        // attach options
        if (opts != null) {
            for (VoteOption o : opts) {
                o.setPoll(p);
                p.getOptions().add(o);
                options.put(o.getId(), o);
            }
        }

        polls.put(p.getId(), p);
        if (creator != null) creator.getCreated().add(p);
        return p;
    }

    public Poll savePoll(Poll p) {
        polls.put(p.getId(), p);
        return p;
    }

    public void deletePoll(UUID pollId) {
        Poll p = polls.remove(pollId);
        if (p == null) return;

        // remove options
        for (VoteOption o : p.getOptions()) {
            options.remove(o.getId());
        }

        // remove votes
        for (Vote v : p.getVotes()) {
            votes.remove(v.getId());
            if (v.getVoter() != null) v.getVoter().getVoted().remove(v);
        }

        if (p.getCreatedBy() != null) p.getCreatedBy().getCreated().remove(p);
    }

    // -------- VoteOptions --------
    public Optional<VoteOption> getOption(UUID id) {
        return Optional.ofNullable(options.get(id));
    }

    public List<VoteOption> listOptionsByPoll(UUID pollId) {
        return getPoll(pollId).map(Poll::getOptions).orElseGet(List::of);
    }

    public Vote castOrChangeVote(UUID pollId, UUID userId, UUID optionId) {
        Poll poll = required(getPoll(pollId), "Poll not found");
        User voter = required(getUser(userId), "User not found");
        VoteOption option = required(getOption(optionId), "Option not found");

        if (!belongsToPoll(option, poll)) {
            throw new IllegalArgumentException("Option does not belong to the poll");
        }
        ensureNotExpired(poll);

        Vote existing = poll.getVotes().stream()
                .filter(v -> v.getVoter() != null && v.getVoter().getId().equals(userId))
                .max(Comparator.comparing(Vote::getPublishedAt))
                .orElse(null);

        if (existing != null) {
            existing.setOption(option);
            if (existing.getValue() == 0) existing.setValue(1);
            existing.setPublishedAt(Instant.now());
            return existing;
        }

        Vote v = Vote.builder()
                .voter(voter)
                .option(option)
                .value(1)
                .publishedAt(Instant.now())
                .build();

        poll.getVotes().add(v);
        voter.getVoted().add(v);
        votes.put(v.getId(), v);
        return v;
    }


    public Vote castOrChangeOptionVote(UUID optionId, UUID userId, int value) {
        if (value != 1 && value != -1) throw new IllegalArgumentException("value must be +1 or -1");

        VoteOption option = required(getOption(optionId), "Option not found");
        User voter = required(getUser(userId), "User not found");
        Poll poll = option.getPoll();
        if (poll == null) throw new IllegalStateException("Option is not attached to any poll");

        ensureNotExpired(poll);

        Vote existing = poll.getVotes().stream()
                .filter(v -> v.getVoter() != null && v.getVoter().getId().equals(userId)
                        && v.getOption() != null && v.getOption().getId().equals(optionId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (existing.getValue() == value) {
                deleteVote(existing.getId());
                return existing;
            }
            existing.setValue(value);
            existing.setPublishedAt(Instant.now());
            return existing;
        }

        Vote v = Vote.builder()
                .voter(voter)
                .option(option)
                .value(value)
                .publishedAt(Instant.now())
                .build();

        poll.getVotes().add(v);
        voter.getVoted().add(v);
        votes.put(v.getId(), v);
        return v;
    }

    public List<Vote> listVotesByPoll(UUID pollId) {
        return getPoll(pollId).map(Poll::getVotes).orElseGet(List::of);
    }

    public Optional<Vote> getVote(UUID id) {
        return Optional.ofNullable(votes.get(id));
    }

    public void deleteVote(UUID voteId) {
        Vote v = votes.remove(voteId);
        if (v == null) return;
        if (v.getVoter() != null) v.getVoter().getVoted().remove(v);
        if (v.getOption() != null && v.getOption().getPoll() != null) {
            v.getOption().getPoll().getVotes().remove(v);
        }
    }

    // -------- Aggregation helpers (for UI) --------
    public int scoreForOption(UUID optionId) {
        return getOption(optionId).map(opt ->
                opt.getPoll().getVotes().stream()
                        .filter(v -> v.getOption() != null && v.getOption().getId().equals(optionId))
                        .mapToInt(Vote::getValue)
                        .sum()
        ).orElse(0);
    }

    public long countForOption(UUID optionId) {
        return getOption(optionId).map(opt ->
                opt.getPoll().getVotes().stream()
                        .filter(v -> v.getOption() != null && v.getOption().getId().equals(optionId))
                        .count()
        ).orElse(0L);
    }


    // -------- helpers --------
    private static <T> T required(Optional<T> opt, String msg) {
        return opt.orElseThrow(() -> new NoSuchElementException(msg));
    }

    private static boolean belongsToPoll(VoteOption option, Poll poll) {
        return option.getPoll() != null && poll.getId().equals(option.getPoll().getId());
    }

    private static void ensureNotExpired(Poll poll) {
        if (poll.getValidUntil() != null && poll.getValidUntil().isBefore(Instant.now())) {
            throw new IllegalStateException("Poll has expired");
        }
    }
}
