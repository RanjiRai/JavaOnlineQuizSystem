package FrontEnd.Admin;

import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminDashboard extends Application {
    private Stage stage;

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        setupUI();
    }

    private void setupUI() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
            new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))),
            CornerRadii.EMPTY, Insets.EMPTY)));

        VBox dashboardLayout = createDashboardLayout();
        root.setCenter(dashboardLayout);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.setMaximized(true);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    private VBox createDashboardLayout() {
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(60));

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);

        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +  "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +  "-fx-padding: 40;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0.3, 0, 4);"
        );

        Button manageQuestionsButton = createStyledButton("Manage Questions", "#4CAF50");
        Button viewResultsButton = createStyledButton("View Results", "#2196F3");
        Button backToLoginButton = createStyledButton("Back to Login", "#9C27B0");

        manageQuestionsButton.setOnAction(e -> showQuizSelection());
        viewResultsButton.setOnAction(e -> new ViewResults().start(stage));
        backToLoginButton.setOnAction(e -> returnToLogin());

        buttonContainer.getChildren().addAll(manageQuestionsButton, viewResultsButton, backToLoginButton);
        layout.getChildren().addAll(titleLabel, buttonContainer);
        return layout;
    }

    private void showQuizSelection() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
            new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))),
            CornerRadii.EMPTY, Insets.EMPTY)));

        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(60));

        Label selectLabel = new Label("Select a Quiz");
        selectLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        selectLabel.setTextFill(Color.WHITE);

        ComboBox<String> quizDropdown = new ComboBox<>();
        quizDropdown.setPromptText("Choose a quiz");
        styleComboBox(quizDropdown);

        Button selectButton = createStyledButton("Edit Quiz", "#4CAF50");
        selectButton.setDisable(true);

        Button backButton = createStyledButton("Back to Dashboard", "#9C27B0");

        Map<String, Integer> quizTitles = QuizDao.getQuizTitles();
        if (quizTitles.isEmpty()) {
            showAlert("No quizzes available", "No quizzes found. Please create a quiz first.");
            return;
        }

        quizDropdown.getItems().addAll(quizTitles.keySet());
        quizDropdown.setOnAction(e -> selectButton.setDisable(false));

        selectButton.setOnAction(e -> {
            String selectedQuiz = quizDropdown.getValue();
            int quizId = quizTitles.get(selectedQuiz);
            new ManageQuestions().start(stage, quizId);
        });

        backButton.setOnAction(e -> setupUI());

        VBox card = new VBox(20, selectLabel, quizDropdown, selectButton, backButton);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(450);
        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1);" +  "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0.3, 0, 4);"
        );

        layout.getChildren().add(card);
        root.setCenter(layout);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Select a Quiz");
        stage.setMaximized(true);
        stage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(300);
        button.setPrefHeight(45);
        button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: derive(" + color + ", -15%);" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-radius: 25;" +  "-fx-background-color: " + color + ";" +  "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        ));

        return button;
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setPrefWidth(300);
        comboBox.setStyle(
            "-fx-font-size: 16px;" + "-fx-background-radius: 8;" + "-fx-background-color: white;" +
            "-fx-border-color: #ddd;" + "-fx-border-radius: 8;" +  "-fx-padding: 10;"
        );
    }

    private void returnToLogin() {
        AdminLogin login = new AdminLogin();
        login.start(stage);
        stage.setMaximized(true);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
