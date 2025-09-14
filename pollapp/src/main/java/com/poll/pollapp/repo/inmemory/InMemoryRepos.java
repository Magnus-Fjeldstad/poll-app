package com.poll.pollapp.repo.inmemory;

import com.poll.pollapp.domain.Poll;
import com.poll.pollapp.domain.User;
import com.poll.pollapp.domain.Vote;
import com.poll.pollapp.domain.VoteOption;
import com.poll.pollapp.manager.PollManager;
import com.poll.pollapp.repo.PollRepo;
import com.poll.pollapp.repo.UserRepo;
import com.poll.pollapp.repo.VoteOptionRepo;
import com.poll.pollapp.repo.VoteRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class InMemoryUserRepository implements UserRepo {
    private final PollManager manager;

    public InMemoryUserRepository(PollManager manager) {
        this.manager = manager;
    }

    public List<User> findAll() {
        return manager.listUsers();
    }

    public Optional<User> findById(UUID id) {
        return manager.getUser(id);
    }

    public User save(User u) {
        return manager.saveUser(u);
    }

    public void deleteById(UUID id) {
        manager.deleteUser(id);
    }
}

@Repository
class InMemoryPollRepository implements PollRepo {
    private final PollManager manager;

    public InMemoryPollRepository(PollManager manager) {
        this.manager = manager;
        System.out.println("InMemoryPollRepository.manager@" + System.identityHashCode(manager));

    }

    public List<Poll> findAll() {
        return manager.listPolls();
    }

    public Optional<Poll> findById(UUID id) {
        return manager.getPoll(id);
    }

    public Poll save(Poll p) {
        return manager.savePoll(p);
    }

    public void deleteById(UUID id) {
        manager.deletePoll(id);
    }
}

@Repository
class InMemoryVoteRepository implements VoteRepo {
    private final PollManager manager;

    public InMemoryVoteRepository(PollManager manager) {
        this.manager = manager;
    }

    public Optional<Vote> findById(UUID id) {
        return manager.getVote(id);
    }

    public List<Vote> findByPollId(UUID pollId) {
        return manager.listVotesByPoll(pollId);
    }

    public void deleteById(UUID id) {
        manager.deleteVote(id);
    }
}

@Repository
class InMemoryVoteOptionRepository implements VoteOptionRepo {
    private final PollManager manager;

    public InMemoryVoteOptionRepository(PollManager manager) {
        this.manager = manager;
    }

    public Optional<VoteOption> findById(UUID id) {
        return manager.getOption(id);
    }

    public List<VoteOption> findByPollId(UUID pollId) {
        return manager.listOptionsByPoll(pollId);
    }
}
