package FrontEnd.User;

import BackEnd.User.AuthController;
import application.Main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class UserRegisterView extends Application {

    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#1e2761")),
                        new Stop(1, Color.web("#408EC6"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));

        Label title = new Label("Create a New Account");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Join the quiz community today!");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#E0E0E0"));

        VBox form = createRegisterForm();

        content.getChildren().addAll(title, subtitle, form);
        root.setCenter(content);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                returnToLogin();
            }
        });

        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createRegisterForm() {
        VBox formLayout = new VBox(20);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(40));
        formLayout.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                "-fx-border-color: rgba(255,255,255,0.2);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );
        formLayout.setMaxWidth(450);

        usernameField = createStyledTextField("Username");
        passwordField = createStyledPasswordField("Password");
        confirmPasswordField = createStyledPasswordField("Confirm Password");

        Button registerBtn = createStyledButton("Register", "#4CAF50");
        registerBtn.setOnAction(e -> handleRegistration());

        Button backBtn = createStyledButton("Back to Login", "#2196F3");
        backBtn.setOnAction(e -> returnToLogin());

        messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 14));

        formLayout.getChildren().addAll(usernameField, passwordField, confirmPasswordField, registerBtn, backBtn, messageLabel);
        return formLayout;
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setFont(Font.font("Arial", 14));
        field.setPrefWidth(350);
        field.setStyle(
                "-fx-background-radius: 8;" +
                "-fx-background-color: white;" +
                "-fx-border-color: #ddd;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 10;"
        );
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setFont(Font.font("Arial", 14));
        field.setPrefWidth(350);
        field.setStyle(
                "-fx-background-radius: 8;" +
                "-fx-background-color: white;" +
                "-fx-border-color: #ddd;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 10;"
        );
        return field;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(300);
        button.setPrefHeight(45);
        button.setStyle(
                "-fx-background-radius: 25;" +
                "-fx-background-color: " + color + ";" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-radius: 25;" +
                "-fx-background-color: derive(" + color + ", -15%);" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-radius: 25;" +
                "-fx-background-color: " + color + ";" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        ));

        return button;
    }

    private void returnToLogin() {
        try {
            UserLogin login = new UserLogin();
            Stage loginStage = new Stage();
            login.start(loginStage);
            loginStage.setMaximized(true);
            stage.close();
        } catch (Exception e) {
            showError("Error returning to login: " + e.getMessage());
        }
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        try {
            if (AuthController.registerUser(username, password)) {
                showSuccess("Registration successful! Redirecting to login...");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                javafx.application.Platform.runLater(() -> returnToLogin());
                            }
                        },
                        2000 // 2-second delay
                );
            } else {
                showError("Username already exists");
            }
        } catch (Exception e) {
            showError("Registration failed. Please try again");
            System.err.println("Registration error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.RED);
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.LIGHTGREEN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
