package FrontEnd.Admin;

import BackEnd.Database.DatabaseConnection;
import Models.Question;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizDao {

    public static boolean addQuestion(int quizId, String question, String optionA, String optionB, String optionC, String optionD, String correctOption, String explanation) {
        String query = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.setString(2, question.trim());
            stmt.setString(3, optionA.trim());
            stmt.setString(4, optionB.trim());
            stmt.setString(5, optionC.trim());
            stmt.setString(6, optionD.trim());
            stmt.setString(7, correctOption.trim());
            stmt.setString(8, explanation.trim());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error adding question: " + e.getMessage());
            return false;
        }
    }

    public static List<Question> getQuestionsByQuizId(int quizId) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE quiz_id = ? ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("question_text").trim(),
                        rs.getString("option_a").trim(),
                        rs.getString("option_b").trim(),
                        rs.getString("option_c").trim(),
                        rs.getString("option_d").trim(),
                        rs.getString("correct_option").trim(),
                        rs.getString("explanation") != null ? rs.getString("explanation").trim() : ""
                );
                questionList.add(q);
            }
        } catch (SQLException e) {
            System.err.println(" Error fetching questions: " + e.getMessage());
        }
        return questionList;
    }
    public static boolean deleteQuestion(int questionId) {
        String query = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, questionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error deleting question: " + e.getMessage());
            return false;
        }
    }

    public static Map<String, Integer> getQuizTitles() {
        Map<String, Integer> quizMap = new HashMap<>();
        String query = "SELECT id, title FROM quizzes ORDER BY title ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                quizMap.put(rs.getString("title"), rs.getInt("id"));
            }

        } catch (SQLException e) {
            System.err.println(" Error fetching quiz titles: " + e.getMessage());
        }
        return quizMap;
    }
}