package Models;

import java.sql.Timestamp;

public class Score {
    private String username;
    private int score;
    private int quizId;
    private Timestamp timestamp;
    private int timeTaken; // Time taken in seconds

    public Score(String username, int score, int quizId) {
        this.username = username;
        this.score = score;
        this.quizId = quizId;
    }
    public Score(String username, int score, int quizId, Timestamp timestamp) {
        this(username, score, quizId);
        this.timestamp = timestamp;
    }
    public Score(String username, int score, int quizId, Timestamp timestamp, int timeTaken) {
        this(username, score, quizId, timestamp);
        this.timeTaken = timeTaken;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public int getScore() { return score; }
    public int getQuizId() { return quizId; }
    public Timestamp getTimestamp() { return timestamp; }
    public int getTimeTaken() { return timeTaken; }
    public void setUsername(String username) { this.username = username; }
    public void setScore(int score) { this.score = score; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public void setTimeTaken(int timeTaken) { this.timeTaken = timeTaken; }
    // Helper method to format time as MM:SS
    public String getFormattedTime() {
        int minutes = timeTaken / 60;
        int seconds = timeTaken % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}