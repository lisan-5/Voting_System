package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ElectionLogger {
    private final List<LogEntry> logs = new ArrayList<>();
    private final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logAction(String message) {
        LogEntry entry = new LogEntry(LocalDateTime.now(), message);
        logs.add(entry);
        System.out.println(entry); // For immediate console feedback
    }

    public List<LogEntry> getLogs() {
        return new ArrayList<>(logs);
    }

    private static class LogEntry {
        private final LocalDateTime timestamp;
        private final String message;

        public LogEntry(LocalDateTime timestamp, String message) {
            this.timestamp = timestamp;
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s", 
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                message);
        }
    }
}
