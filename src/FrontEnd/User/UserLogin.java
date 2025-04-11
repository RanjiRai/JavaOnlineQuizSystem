package FrontEnd.User;

import BackEnd.Middleware.AuthService;
import application.Main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class UserLogin extends Application {

    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
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

        Label title = new Label("Welcome Back, Quizzer!");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Log in to continue your journey of knowledge.");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#E0E0E0"));

        VBox loginForm = createLoginForm();

        content.getChildren().addAll(title, subtitle, loginForm);
        root.setCenter(content);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Login");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createLoginForm() {
        VBox formLayout = new VBox(20);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(30));
        formLayout.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" + "-fx-border-color: rgba(255,255,255,0.2);" +
                "-fx-border-width: 1;" + "-fx-border-radius: 12;" + "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );
        formLayout.setMaxWidth(400);

        usernameField = createStyledTextField("Username");
        passwordField = createStyledPasswordField("Password");

        messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        messageLabel.setTextFill(Color.RED);

        Button loginBtn = createRoleButton("Login", "#4CAF50");
        loginBtn.setOnAction(e -> handleLogin());

        Button registerBtn = createRoleButton("Register", "#2196F3");
        registerBtn.setOnAction(e -> {
            new UserRegisterView().start(new Stage());
            stage.close();
        });

        Button backBtn = createRoleButton("Back to Homepage", "#FF5722");
        backBtn.setOnAction(e -> returnToHomepage());

        VBox buttonBox = new VBox(15, loginBtn, registerBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        formLayout.getChildren().addAll(usernameField, passwordField, buttonBox, messageLabel);
        return formLayout;
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setFont(Font.font("Arial", 14));
        field.setPrefWidth(300);
        field.setStyle(
                "-fx-background-radius: 8;" + "-fx-background-color: white;" +
                "-fx-border-color: #ddd;" + "-fx-border-radius: 8;" + "-fx-padding: 10;"
        );
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setFont(Font.font("Arial", 14));
        field.setPrefWidth(300);
        field.setStyle(
                "-fx-background-radius: 8;" + "-fx-background-color: white;" +
                "-fx-border-color: #ddd;" + "-fx-border-radius: 8;" + "-fx-padding: 10;"
        );
        return field;
    }

    private Button createRoleButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        button.setStyle(
                "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" +"-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-radius: 25;" +  "-fx-background-color: derive(" + color + ", -15%);" + "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-radius: 25;" +  "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
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
            stage.close();
        } catch (Exception e) {
            showError("Error returning to homepage");
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty");
            return;
        }

        try {
            if (AuthService.validateUser(username, password, "USER")) {
                new UserDashboard(stage, username).start();
            } else {
                showError("Invalid username or password");
            }
        } catch (Exception e) {
            showError("Login failed. Please try again");
        }
    }

    private void showError(String message) {
        messageLabel.setText(message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
