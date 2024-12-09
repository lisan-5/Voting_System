package model;

import java.util.UUID;

public class Candidate {
    private final String id;
    private final String name;
    private final String party;
    private final String manifesto;
    private int voteCount;
    private String imageUrl;
    private String background;

    public Candidate(String name, String party, String manifesto, String imageUrl, String background) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.party = party;
        this.manifesto = manifesto;
        this.imageUrl = imageUrl;
        this.background = background;
        this.voteCount = 0;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getParty() { return party; }
    public String getManifesto() { return manifesto; }
    public int getVoteCount() { return voteCount; }
    public String getImageUrl() { return imageUrl; }
    public String getBackground() { return background; }

    public synchronized void incrementVote() {
        this.voteCount++;
    }

    @Override
    public String toString() {
        return String.format("Candidate: %s (%s) - %s votes", name, party, voteCount);
    }
}
