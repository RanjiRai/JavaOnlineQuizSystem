package BackEnd.User;

import Models.Question;
import Models.Score;
import java.sql.Timestamp;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizController {
    private static long startTime;
    private static Timer quizTimer;
    private static final int TIME_LIMIT = 180; // 3 minutes in seconds
    private static int timeRemaining;
    private static boolean timeExpired = false;

    public static void startQuiz() {
        startTime = System.currentTimeMillis();
        timeRemaining = TIME_LIMIT;
        timeExpired = false;
        System.out.println("Quiz started at: " + new Timestamp(startTime));
        
        quizTimer = new Timer();
        quizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                if (timeRemaining <= 0) {
                    timeExpired = true;
                    quizTimer.cancel();
                    System.out.println("Time's up! Quiz auto-submitted.");
                }
            }
        }, 1000, 1000);
    }

    public static List<Question> getQuizQuestions(int quizId) {
        return QuizDAO.getQuestionsByQuizId(quizId);
    }

    public static int calculateScore(List<Question> questions, String[] userAnswers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && questions.get(i).getCorrectOption().equalsIgnoreCase(userAnswers[i])) {
                score += 10;
            }
        }
        return score;
    }

    public static boolean saveScore(String username, int quizId, int score) {
        stopTimer();
        int timeTaken = TIME_LIMIT - timeRemaining;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Score scoreObj = new Score(username, score, quizId, timestamp, timeTaken);
        return ScoreDAO.saveUserScore(scoreObj);
    }

    public static void stopTimer() {
        if (quizTimer != null) {
            quizTimer.cancel();
        }
    }

    public static int getTimeRemaining() {
        return timeRemaining;
    }

    public static boolean isTimeExpired() {
        return timeExpired;
    }
}