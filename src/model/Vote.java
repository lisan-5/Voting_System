// src/model/Vote.java
package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Vote {
    private final String id;
    private final String voterId;
    private final String candidateId;
    private final LocalDateTime timestamp;
    private final String votingStation;

    public Vote(String voterId, String candidateId, String votingStation) {
        this.id = UUID.randomUUID().toString();
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.timestamp = LocalDateTime.now();
        this.votingStation = votingStation;
    }

    // Getters
    public String getId() { return id; }
    public String getVoterId() { return voterId; }
    public String getCandidateId() { return candidateId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getVotingStation() { return votingStation; }
}
