package FrontEnd.User;

import BackEnd.User.QuizDAO;
import BackEnd.User.QuizController;
import Models.Question;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizView {
    private Stage stage;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String[] userAnswers;
    private Label questionLabel;
    private RadioButton optionA, optionB, optionC, optionD;
    private ToggleGroup optionsGroup;
    private Button nextButton, prevButton;
    private String username;
    private String selectedQuizTitle;
    private int selectedQuizId;
    private Label timerLabel;
    private Timeline timeline;

    public QuizView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
        showQuizSelectionScreen();
    }

    private void showQuizSelectionScreen() {
        BorderPane root = new BorderPane();

        // Gradient background matching the Main class
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),  // Dark blue
                new Stop(1, Color.web("#408EC6"))   // Lighter blue
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        Map<String, Integer> quizTitles = QuizDAO.getQuizTitles();

        // Title
        Label titleLabel = new Label("SELECT A QUIZ");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);
        DropShadow shadow = new DropShadow(10, Color.BLACK);
        titleLabel.setEffect(shadow);

        VBox layout = new VBox(40);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(60, 50, 50, 50));
        layout.setMaxWidth(1000);

        if (quizTitles.isEmpty()) {
            // No quizzes available
            VBox messageBox = createFeatureBox("No Quizzes Available", "There are currently no quizzes available to take.");
            Button backButton = createStyledButton("Back to Dashboard", "#FF5722");

            backButton.setOnAction(e -> new UserDashboard(stage, username).start());

            layout.getChildren().addAll(titleLabel, messageBox, backButton);
        } else {
            // Quiz selection
            Label chooseLabel = new Label("Choose a quiz to start your challenge:");
            chooseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            chooseLabel.setTextFill(Color.web("#E0E0E0"));

            ComboBox<String> quizDropdown = new ComboBox<>();
            List<Map.Entry<String, Integer>> sortedQuizzes = new ArrayList<>(quizTitles.entrySet());
            sortedQuizzes.sort(Map.Entry.comparingByValue());
            for (Map.Entry<String, Integer> entry : sortedQuizzes) {
                quizDropdown.getItems().add(entry.getKey());
            }
            quizDropdown.setPromptText("Select a quiz");
            styleComboBox(quizDropdown);

            Button startButton = createStyledButton("Start Quiz", "#4CAF50");
            startButton.setDisable(true);
            Button backButton = createStyledButton("Back to Dashboard", "#FF5722");

            quizDropdown.setOnAction(e -> {
                if (quizDropdown.getValue() != null) {
                    startButton.setDisable(false);
                    selectedQuizTitle = quizDropdown.getValue();
                }
            });

            startButton.setOnAction(e -> {
                selectedQuizId = quizTitles.get(selectedQuizTitle);
                startQuiz(selectedQuizId);
            });

            backButton.setOnAction(e -> new UserDashboard(stage, username).start());

            layout.getChildren().addAll(titleLabel, chooseLabel, quizDropdown, startButton, backButton);
        }

        root.setCenter(layout);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new UserDashboard(stage, username).start();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Quiz Selection");
        stage.setMaximized(true);
        stage.show();
    }

    private void startQuiz(int quizId) {
        this.questions = QuizDAO.getQuestionsByQuizId(quizId); // Fetches 10 questions as per LIMIT 10
        if (questions.isEmpty() || questions.size() < 10) {
            showEmptyQuizScreen();
            return;
        }
        userAnswers = new String[questions.size()];
        QuizController.startQuiz();
        setupQuizUI();
        loadQuestion();
    }

    private void showEmptyQuizScreen() {
        BorderPane root = new BorderPane();

        // Gradient background
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        // Title
        Label titleLabel = new Label("NO QUESTIONS");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);
        DropShadow shadow = new DropShadow(10, Color.BLACK);
        titleLabel.setEffect(shadow);

        // Message
        VBox messageBox = createFeatureBox("No Questions Available", "There are no questions available for this quiz.");

        // Back button
        Button backButton = createStyledButton("Back to Dashboard", "#FF5722");
        backButton.setOnAction(e -> new UserDashboard(stage, username).start());

        VBox layout = new VBox(40, titleLabel, messageBox, backButton);
        layout.setPadding(new Insets(60, 50, 50, 50));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(1000);

        root.setCenter(layout);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new UserDashboard(stage, username).start();
            }
        });
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void setupQuizUI() {
        BorderPane root = new BorderPane();

        // Gradient background
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        // Timer
        timerLabel = new Label("03:00");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timerLabel.setTextFill(Color.WHITE);
        DropShadow timerShadow = new DropShadow(5, Color.BLACK);
        timerLabel.setEffect(timerShadow);

        // Question label
        questionLabel = new Label();
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        questionLabel.setTextFill(Color.WHITE);
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(800);

        // Options
        optionsGroup = new ToggleGroup();
        optionA = createStyledRadioButton(optionsGroup);
        optionB = createStyledRadioButton(optionsGroup);
        optionC = createStyledRadioButton(optionsGroup);
        optionD = createStyledRadioButton(optionsGroup);

        // Navigation buttons
        prevButton = createStyledButton("Previous", "#FF5722");
        prevButton.setDisable(true);
        prevButton.setOnAction(e -> handlePrevious());

        nextButton = createStyledButton("Next", "#4CAF50");
        nextButton.setDisable(true);
        nextButton.setOnAction(e -> handleNext());

        optionsGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) ->
                nextButton.setDisable(newVal == null));

        VBox optionsBox = new VBox(15, optionA, optionB, optionC, optionD);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        optionsBox.setMaxWidth(800);

        HBox navigationBox = new HBox(40, prevButton, nextButton);
        navigationBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30, timerLabel, questionLabel, optionsBox, navigationBox);
        layout.setPadding(new Insets(60, 50, 50, 50));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(1000);

        root.setCenter(layout);

        startTimer();

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                QuizController.stopTimer();
                new UserDashboard(stage, username).start();
            }
        });
        stage.setScene(scene);
        stage.setTitle("Quiz: " + selectedQuizTitle);
        stage.setMaximized(true);
        stage.show();
    }

    private void startTimer() {
        timerLabel.setText("03:00");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int remaining = QuizController.getTimeRemaining();
            if (remaining <= 0 || QuizController.isTimeExpired()) {
                timeline.stop();
                timerLabel.setText("00:00");
                showResults();
            } else {
                int minutes = remaining / 60;
                int seconds = remaining % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                if (remaining <= 30) {
                    timerLabel.setTextFill(Color.web("#FF5722")); // Red when time is low
                } else {
                    timerLabel.setTextFill(Color.WHITE);
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + q.getQuestionText());
            optionA.setText("A) " + q.getOptionA());
            optionB.setText("B) " + q.getOptionB());
            optionC.setText("C) " + q.getOptionC());
            optionD.setText("D) " + q.getOptionD());
            optionsGroup.selectToggle(null);

            if (userAnswers[currentQuestionIndex] != null) {
                switch (userAnswers[currentQuestionIndex]) {
                    case "A": optionA.setSelected(true); break;
                    case "B": optionB.setSelected(true); break;
                    case "C": optionC.setSelected(true); break;
                    case "D": optionD.setSelected(true); break;
                }
            }

            prevButton.setDisable(currentQuestionIndex == 0);
            nextButton.setText(currentQuestionIndex == questions.size() - 1 ? "Submit" : "Next");
        } else {
            showResults();
        }
    }

    private void handleNext() {
        if (optionsGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
            String selectedText = selected.getText().substring(0, 1);
            userAnswers[currentQuestionIndex] = selectedText;
        } else {
            userAnswers[currentQuestionIndex] = "";
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            loadQuestion();
        } else {
            showResults();
        }
    }

    private void handlePrevious() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion();
        }
    }

    private void showResults() {
        timeline.stop();
        QuizController.stopTimer();

        int score = QuizController.calculateScore(questions, userAnswers);
        if (username != null) {
            boolean saved = QuizController.saveScore(username, selectedQuizId, score);
            if (!saved) System.err.println("Failed to save score in database");
        }

        BorderPane root = new BorderPane();

        // Gradient background
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        // Title
        Label titleLabel = new Label("QUIZ COMPLETED!");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);
        DropShadow shadow = new DropShadow(10, Color.BLACK);
        titleLabel.setEffect(shadow);

        // Score
        Label scoreLabel = new Label("Your Score: " + score + "/" + (questions.size() * 10));
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        scoreLabel.setTextFill(score >= (questions.size() * 5) ? Color.web("#4CAF50") : Color.web("#FF5722")); // Green if score >= 50%, else red
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setPadding(new Insets(10));

        // Results
        VBox resultsBox = new VBox(20);
        resultsBox.setAlignment(Pos.CENTER);
        resultsBox.setPadding(new Insets(20));

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String userAnswer = userAnswers[i] != null ? userAnswers[i] : "Not Answered";
            String correctAnswer = q.getCorrectOption();
            String explanation = q.getExplanation().isEmpty() ? "No explanation provided." : q.getExplanation();

            Label questionResult = new Label(
                "Q" + (i + 1) + ": " + q.getQuestionText() + "\n" +
                "Your Answer: " + userAnswer + "\n" +
                "Correct Answer: " + correctAnswer + "\n" +
                "Explanation: " + explanation
            );
            questionResult.setFont(Font.font("Arial", 16));
            questionResult.setTextFill(Color.WHITE);
            questionResult.setWrapText(true);
            questionResult.setMaxWidth(800);
            questionResult.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" + "-fx-border-color: rgba(255,255,255,0.2);" +
                "-fx-border-width: 1;" + "-fx-border-radius: 12;" + "-fx-background-radius: 12;" + "-fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
            );
            questionResult.setTextFill(userAnswer.equals(correctAnswer) ?
                Color.web("#4CAF50") : Color.web("#FF5722"));
            resultsBox.getChildren().add(questionResult);
        }

        // Back button
        Button dashboardButton = createStyledButton("Back to Dashboard", "#FF5722");
        dashboardButton.setOnAction(e -> new UserDashboard(stage, username).start());

        ScrollPane scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox layout = new VBox(40, titleLabel, scoreLabel, scrollPane, dashboardButton);
        layout.setPadding(new Insets(60, 50, 50, 50));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(1000);

        root.setCenter(layout);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new UserDashboard(stage, username).start();
            }
        });
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(220);
        button.setPrefHeight(50);
        button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: derive(" + color + ", -15%);" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        ));

        return button;
    }

    private RadioButton createStyledRadioButton(ToggleGroup group) {
        RadioButton rb = new RadioButton();
        rb.setToggleGroup(group);
        rb.setFont(Font.font("Arial", 16));
        rb.setTextFill(Color.WHITE);
        rb.setStyle(
            "-fx-padding: 10;" + "-fx-background-radius: 5;" +"-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-color: rgba(255,255,255,0.2);" +"-fx-border-width: 1;" + "-fx-border-radius: 5;"
        );
        return rb;
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setStyle(
            "-fx-font-size: 16px;" + "-fx-background-radius: 5;" + "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-color: rgba(255,255,255,0.2);" + "-fx-border-width: 1;" +"-fx-border-radius: 5;" +"-fx-text-fill: white;"
        );
        comboBox.setPrefWidth(400);
    }

    private VBox createFeatureBox(String title, String description) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(25, 20, 25, 20));
        box.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" + "-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" + "-fx-border-radius: 12;" + "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );
        box.setPrefWidth(400);
        box.setPrefHeight(200);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.WHITE);

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 16));
        descLabel.setTextFill(Color.web("#E0E0E0"));
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setMaxWidth(380);

        box.getChildren().addAll(titleLabel, descLabel);
        return box;
    }
}