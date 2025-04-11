package BackEnd.User;

import Models.Score;

import java.util.List;

public class LeaderBoardController {

    public static List<Score> getTopScores() {
        return ScoreDAO.getTopScores();
    }

    public static List<Score> getTopScores(int quizId) {
        return ScoreDAO.getTopScores(quizId);
    }
}