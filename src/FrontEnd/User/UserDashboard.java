package FrontEnd.User;

import application.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserDashboard {
    private Stage primaryStage;
    private String username;

    public UserDashboard(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
    }

    public void start() {
        primaryStage.setTitle("User Dashboard - " + username);

        BorderPane root = new BorderPane();

        // Gradient background like the main screen
        BackgroundFill gradientFill = new BackgroundFill(
                new LinearGradient(
                        0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#1e2761")),
                        new Stop(1, Color.web("#408EC6"))
                ),
                CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        VBox dashboardContent = createDashboardContent();
        root.setCenter(dashboardContent);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.show();
    }

    private VBox createDashboardContent() {
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(60));

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        welcomeLabel.setTextFill(Color.WHITE);

        Button startQuizButton = createStyledButton("Start Quiz", "#4CAF50");
        startQuizButton.setOnAction(e -> new QuizView(primaryStage, username));

        Button viewLeaderboardButton = createStyledButton("View Leaderboard", "#2196F3");
        viewLeaderboardButton.setOnAction(e -> new LeaderboardView(primaryStage, username).start());

        Button logoutButton = createStyledButton("Logout", "#F44336");
        logoutButton.setOnAction(e -> new UserLogin().start(primaryStage));

        Button backButton = createStyledButton("Back to Homepage", "#9C27B0");
        backButton.setOnAction(e -> returnToHomepage());

        layout.getChildren().addAll(
                welcomeLabel,
                startQuizButton,
                viewLeaderboardButton,
                logoutButton,
                backButton
        );
        return layout;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(300);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

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

    private void returnToHomepage() {
        try {
            Main main = new Main();
            Stage mainStage = new Stage();
            main.start(mainStage);
            mainStage.setMaximized(true);
            primaryStage.close();
        } catch (Exception e) {
            System.err.println("Error returning to homepage: " + e.getMessage());
        }
    }
}
