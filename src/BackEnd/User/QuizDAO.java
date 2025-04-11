package BackEnd.User;

import BackEnd.Database.DatabaseConnection;
import Models.Question;

import java.sql.*;
import java.util.*;

public class QuizDAO {

    public static Map<String, Integer> getQuizTitles() {
        Map<String, Integer> quizzes = new LinkedHashMap<>();
        String query = "SELECT id, title FROM quizzes ORDER BY title ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                quizzes.put(rs.getString("title").trim(), rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quiz titles: " + e.getMessage());
        }
        return quizzes;
    }

    public static List<Question> getQuestionsByQuizId(int quizId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation " +
                       "FROM questions WHERE quiz_id = ? ORDER BY RAND() LIMIT 10"; // Changed to 10 questions

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                questions.add(new Question(
                    rs.getInt("id"),
                    rs.getString("question_text").trim(),
                    rs.getString("option_a").trim(),
                    rs.getString("option_b").trim(),
                    rs.getString("option_c").trim(),
                    rs.getString("option_d").trim(),
                    rs.getString("correct_option").trim(),
                    rs.getString("explanation") != null ? rs.getString("explanation").trim() : ""
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }

    public static boolean addQuestion(Question question, int quizId) {
        String sql = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quizId);
            pstmt.setString(2, question.getQuestionText().trim());
            pstmt.setString(3, question.getOptionA().trim());
            pstmt.setString(4, question.getOptionB().trim());
            pstmt.setString(5, question.getOptionC().trim());
            pstmt.setString(6, question.getOptionD().trim());
            pstmt.setString(7, question.getCorrectOption().trim());
            pstmt.setString(8, question.getExplanation().trim());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding question: " + e.getMessage());
            return false;
        }
    }

    public static Map<String, Integer> getQuizScores(int quizId) {
        Map<String, Integer> scores = new LinkedHashMap<>();
        String query = "SELECT username, score FROM scores WHERE quiz_id = ? ORDER BY score DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                scores.put(rs.getString("username"), rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scores: " + e.getMessage());
        }
        return scores;
    }
}