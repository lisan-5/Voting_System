package service;

import model.Voter;
import exception.VotingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationService {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public String login(Voter voter, String password) throws VotingException {
        if (!voter.verifyPassword(password)) {
            throw new VotingException("Invalid credentials");
        }
        
        String sessionId = generateSessionId();
        sessions.put(sessionId, voter.getId());
        return sessionId;
    }

    public void logout(String sessionId) {
        sessions.remove(sessionId);
    }

    public String getVoterId(String sessionId) throws VotingException {
        String voterId = sessions.get(sessionId);
        if (voterId == null) {
            throw new VotingException("Invalid session");
        }
        return voterId;
    }

    private String generateSessionId() {
        return java.util.UUID.randomUUID().toString();
    }
}
