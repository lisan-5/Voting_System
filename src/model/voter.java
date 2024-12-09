package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Voter {
    private final String id;
    private final String name;
    private final String email;
    private final String nationalId;
    private final LocalDateTime registrationDate;
    private boolean hasVoted;
    private String password;
    private VoterStatus status;

    public enum VoterStatus {
        PENDING_VERIFICATION,
        VERIFIED,
        BLOCKED
    }

    public Voter(String name, String email, String nationalId, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.nationalId = nationalId;
        this.password = password;
        this.registrationDate = LocalDateTime.now();
        this.status = VoterStatus.PENDING_VERIFICATION;
        this.hasVoted = false;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getNationalId() { return nationalId; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public boolean hasVoted() { return hasVoted; }
    public VoterStatus getStatus() { return status; }

    // Setters
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }
    public void setStatus(VoterStatus status) { this.status = status; }

    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
