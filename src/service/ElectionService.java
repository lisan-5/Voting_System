package service;

import model.*;
import exception.VotingException;
import util.ValidationUtil;
import util.ElectionLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ElectionService {
    private final Map<String, Candidate> candidates;
    private final Map<String, Voter> voters;
    private final Set<Vote> votes;
    private final ElectionLogger logger;
    private ElectionStatus status;
    private final LocalDateTime electionDate;
    private final String electionName;

    public enum ElectionStatus {
        SETUP, REGISTRATION_OPEN, VOTING_OPEN, CLOSED
    }

    public ElectionService(String electionName, LocalDateTime electionDate) {
        this.candidates = new ConcurrentHashMap<>();
        this.voters = new ConcurrentHashMap<>();
        this.votes = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.logger = new ElectionLogger();
        this.status = ElectionStatus.SETUP;
        this.electionName = electionName;
        this.electionDate = electionDate;
    }

    public synchronized void addCandidate(Candidate candidate) throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.SETUP);
        candidates.put(candidate.getId(), candidate);
        logger.logAction("Added candidate: " + candidate.getName());
    }

    public synchronized void registerVoter(Voter voter) throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.REGISTRATION_OPEN);
        if (voters.containsKey(voter.getNationalId())) {
            throw new VotingException("Voter already registered");
        }
        voters.put(voter.getId(), voter);
        logger.logAction("Registered voter: " + voter.getName());
    }

    public synchronized void castVote(String voterId, String candidateId, String votingStation) 
            throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.VOTING_OPEN);
        
        Voter voter = voters.get(voterId);
        ValidationUtil.validateVoter(voter);
        
        Candidate candidate = candidates.get(candidateId);
        ValidationUtil.validateCandidate(candidate);

        Vote vote = new Vote(voterId, candidateId, votingStation);
        votes.add(vote);
        voter.setHasVoted(true);
        candidate.incrementVote();
        
        logger.logAction("Vote cast by voter ID: " + voterId);
    }

    public Map<String, Integer> getResults() throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.CLOSED);
        return candidates.values().stream()
            .collect(Collectors.toMap(
                Candidate::getName,
                Candidate::getVoteCount
            ));
    }

    public List<Candidate> getTopCandidates(int limit) {
        return candidates.values().stream()
            .sorted(Comparator.comparingInt(Candidate::getVoteCount).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    public ElectionStatistics getStatistics() {
        int totalVoters = voters.size();
        int totalVotes = votes.size();
        double turnout = totalVoters == 0 ? 0 : (double) totalVotes / totalVoters * 100;
        
        return new ElectionStatistics(
            totalVoters,
            totalVotes,
            turnout,
            candidates.size(),
            status
        );
    }

    // Status management methods
    public void openRegistration() throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.SETUP);
        status = ElectionStatus.REGISTRATION_OPEN;
        logger.logAction("Voter registration opened");
    }

    public void openVoting() throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.REGISTRATION_OPEN);
        status = ElectionStatus.VOTING_OPEN;
        logger.logAction("Voting opened");
    }

    public void closeElection() throws VotingException {
        ValidationUtil.validateElectionStatus(status, ElectionStatus.VOTING_OPEN);
        status = ElectionStatus.CLOSED;
        logger.logAction("Election closed");
    }

    // Inner class for election statistics
    public static class ElectionStatistics {
        private final int totalVoters;
        private final int totalVotes;
        private final double turnoutPercentage;
        private final int totalCandidates;
        private final ElectionStatus status;

        public ElectionStatistics(int totalVoters, int totalVotes, double turnoutPercentage,
                                int totalCandidates, ElectionStatus status) {
            this.totalVoters = totalVoters;
            this.totalVotes = totalVotes;
            this.turnoutPercentage = turnoutPercentage;
            this.totalCandidates = totalCandidates;
            this.status = status;
        }

        // Getters
        public int getTotalVoters() { return totalVoters; }
        public int getTotalVotes() { return totalVotes; }
        public double getTurnoutPercentage() { return turnoutPercentage; }
        public int getTotalCandidates() { return totalCandidates; }
        public ElectionStatus getStatus() { return status; }
    }
}
