package ui;

import model.*;
import service.*;
import exception.VotingException;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ElectionUI {
    private final Scanner scanner;
    private final ElectionService electionService;
    private final AuthenticationService authService;
    private String currentSession;

    public ElectionUI() {
        this.scanner = new Scanner(System.in);
        this.electionService = new ElectionService("General Election 2024", 
            LocalDateTime.now().plusDays(30));
        this.authService = new AuthenticationService();
    }

    public void start() {
        while (true) {
            try {
                showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> adminMenu();
                    case 2 -> voterMenu();
                    case 3 -> viewResults();
                    case 4 -> {
                        System.out.println("Exiting system...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Election System ===");
        System.out.println("1. Admin Access");
        System.out.println("2. Voter Access");
        System.out.println("3. View Results");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Candidate");
            System.out.println("2. View All Candidates");
            System.out.println("3. Open Registration");
            System.out.println("4. Open Voting");
            System.out.println("5. Close Election");
            System.out.println("6. View Statistics");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addCandidate();
                    case 2 -> viewCandidates();
                    case 3 -> electionService.openRegistration();
                    case 4 -> electionService.openVoting();
                    case 5 -> electionService.closeElection();
                    case 6 -> viewStatistics();
                    case 7 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
    }

    private void voterMenu() {
        while (true) {
            System.out.println("\n=== Voter Menu ===");
            System.out.println("1. Register to Vote");
            System.out.println("2. Login");
            System.out.println("3. Cast Vote");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> registerVoter();
                    case 2 -> login();
                    case 3 -> castVote();
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
    }

    private void addCandidate() throws VotingException {
        System.out.println("\n=== Add New Candidate ===");
        System.out.print("Enter candidate name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter party: ");
        String party = scanner.nextLine();
        
        System.out.print("Enter manifesto: ");
        String manifesto = scanner.nextLine();
        
        System.out.print("Enter image URL: ");
        String imageUrl = scanner.nextLine();
        
        System.out.print("Enter background: ");
        String background = scanner.nextLine();

        Candidate candidate = new Candidate(name, party, manifesto, imageUrl, background);
        electionService.addCandidate(candidate);
        System.out.println("Candidate added successfully!");
    }

    private void registerVoter() throws VotingException {
        System.out.println("\n=== Voter Registration ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter your national ID: ");
        String nationalId = scanner.nextLine();
        
        System.out.print("Create password: ");
        String password = scanner.nextLine();

        Voter voter = new Voter(name, email, nationalId, password);
        electionService.registerVoter(voter);
        System.out.println("Registration successful! Please wait for verification.");
    }

    private void login() throws VotingException {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // In a real system, you would look up the voter by email
        // This is simplified for demonstration
        currentSession = "dummy-session";
        System.out.println("Login successful!");
    }

    private void castVote() throws VotingException {
        if (currentSession == null) {
            throw new VotingException("Please login first");
        }

        viewCandidates();
        System.out.print("Enter candidate ID to vote: ");
        String candidateId = scanner.nextLine();

        electionService.castVote(
            authService.getVoterId(currentSession),
            candidateId,
            "ONLINE"
        );
        System.out.println("Vote cast successfully!");
    }

    private void viewCandidates() {
        System.out.println("\n=== Candidates ===");
        // Implementation to display candidates
    }

    private void viewResults() throws VotingException {
        System.out.println("\n=== Election Results ===");
        Map<String, Integer> results = electionService.getResults();
        results.forEach((name, votes) -> 
            System.out.printf("%s: %d votes%n", name, votes));
    }

    private void viewStatistics() {
        ElectionService.ElectionStatistics stats = electionService.getStatistics();
        System.out.println("\n=== Election Statistics ===");
        System.out.printf("Total Voters: %d%n", stats.getTotalVoters());
        System.out.printf("Total Votes: %d%n", stats.getTotalVotes());
        System.out.printf("Turnout: %.2f%%%n", stats.getTurnoutPercentage());
        System.out.printf("Total Candidates: %d%n", stats.getTotalCandidates());
        System.out.printf("Election Status: %s%n", stats.getStatus());
    }
}
