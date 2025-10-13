package com.poll.pollapp.listener;

import com.poll.pollapp.manager.PollManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class VoteEventListener {

    private final PollManager pollManager;

    public VoteEventListener(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @RabbitListener(queues = "pollApp.votes")
    public void handleVoteEvent(Map<String, Object> event) {
        try {
            System.out.println("📩 Rabbit message: " + event);
            String type = (String) event.get("type");
            if ("VoteCast".equals(type)) {
                UUID pollId = UUID.fromString((String) event.get("pollId"));
                UUID optionId = UUID.fromString((String) event.get("optionId"));
                String voter = (String) event.get("voter");
                System.out.println("Received VoteCast from RabbitMQ for poll " + pollId + " (" + voter + ")");
                pollManager.castAnonymousVote(pollId, optionId);
            } else {
                System.out.println("ℹ️ Ignored message type: " + type);
            }
        } catch (Exception e) {
            System.err.println("Error handling Rabbit message: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
