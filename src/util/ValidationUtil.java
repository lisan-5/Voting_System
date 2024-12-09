package util;

import model.*;
import exception.VotingException;
import service.ElectionService.ElectionStatus;

public class ValidationUtil {
    public static void validateElectionStatus(ElectionStatus current, ElectionStatus required) 
            throws VotingException {
        if (current != required) {
            throw new VotingException("Invalid election status. Required: " + required + 
                                    ", Current: " + current);
        }
    }

    public static void validateVoter(Voter voter) throws VotingException {
        if (voter == null) {
            throw new VotingException("Voter not found");
        }
        if (voter.hasVoted()) {
            throw new VotingException("Voter has already cast their vote");
        }
        if (voter.getStatus() != Voter.VoterStatus.VERIFIED) {
            throw new VotingException("Voter is not verified");
        }
    }

    public static void validateCandidate(Candidate candidate) throws VotingException {
        if (candidate == null) {
            throw new VotingException("Candidate not found");
        }
    }
}
