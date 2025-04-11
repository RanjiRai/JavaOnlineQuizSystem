package BackEnd.User;

import BackEnd.Database.DatabaseConnection;
import Models.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {
    public static boolean saveUserScore(Score score) {
        String getUserIdQuery = "SELECT id FROM users WHERE username = ?";
        String checkScoreQuery = "SELECT score, time_taken FROM scores WHERE user_id = ? AND quiz_id = ?";
        String insertQuery = "INSERT INTO scores (user_id, quiz_id, score, date, time_taken) VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE scores SET score = ?, date = ?, time_taken = ? WHERE user_id = ? AND quiz_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement getUserStmt = conn.prepareStatement(getUserIdQuery)) {
            
            conn.setAutoCommit(false);
            getUserStmt.setString(1, score.getUsername());
            
            try (ResultSet rs = getUserStmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    
                    try (PreparedStatement checkScoreStmt = conn.prepareStatement(checkScoreQuery)) {
                        checkScoreStmt.setInt(1, userId);
                        checkScoreStmt.setInt(2, score.getQuizId());
                        
                        try (ResultSet scoreRs = checkScoreStmt.executeQuery()) {
                            if (scoreRs.next()) {
                                int existingScore = scoreRs.getInt("score");
                                int existingTime = scoreRs.getInt("time_taken");
                                
                                if (score.getScore() > existingScore || 
                                    (score.getScore() == existingScore && score.getTimeTaken() < existingTime)) {
                                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                        updateStmt.setInt(1, score.getScore());
                                        updateStmt.setTimestamp(2, score.getTimestamp());
                                        updateStmt.setInt(3, score.getTimeTaken());
                                        updateStmt.setInt(4, userId);
                                        updateStmt.setInt(5, score.getQuizId());
                                        updateStmt.executeUpdate();
                                    }
                                }
                            } else {
                                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                    insertStmt.setInt(1, userId);
                                    insertStmt.setInt(2, score.getQuizId());
                                    insertStmt.setInt(3, score.getScore());
                                    insertStmt.setTimestamp(4, score.getTimestamp());
                                    insertStmt.setInt(5, score.getTimeTaken());
                                    insertStmt.executeUpdate();
                                }
                            }
                        }
                    }
                    conn.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving score: " + e.getMessage());
            return false;
        }
        return false;
    }

    public static List<Score> getTopScores() {
        return getTopScores(-1);
    }

    public static List<Score> getTopScores(int quizId) {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT u.username, s.score, s.quiz_id, s.date, s.time_taken " +
                      "FROM scores s " +
                      "JOIN users u ON s.user_id = u.id " +
                      (quizId > 0 ? "WHERE s.quiz_id = ? " : "") +
                      "ORDER BY s.score DESC, s.time_taken ASC " +
                      "LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (quizId > 0) {
                stmt.setInt(1, quizId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(new Score(
                        rs.getString("username"), 
                        rs.getInt("score"),
                        rs.getInt("quiz_id"),
                        rs.getTimestamp("date"),
                        rs.getInt("time_taken")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scores: " + e.getMessage());
        }
        return scores;
    }
}