package application;

import FrontEnd.User.UserLogin;
import FrontEnd.Admin.AdminLogin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
      private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Online Quiz");

        BorderPane root = new BorderPane();
        
        // Gradient background 
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),  // Dark blue
                new Stop(1, Color.web("#408EC6"))   // Lighter blue
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        VBox mainContent = createMainContent();
        root.setCenter(mainContent);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Keep the window maximized
        primaryStage.show();
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(40);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(60, 50, 50, 50));
        mainContent.setMaxWidth(1000);

        VBox titleBox = createTitleBox();
        GridPane featureGrid = createFeatureGrid();
        VBox roleSelection = createRoleSelection();

        mainContent.getChildren().addAll(titleBox, featureGrid, roleSelection);
        return mainContent;
    }

    private VBox createTitleBox() {
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);

        Label mainTitle = new Label("Online Quiz");
        mainTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        mainTitle.setTextFill(Color.WHITE);

        Label subtitle = new Label("Welcome to the reserved platform where brilliance collides with knowledge.");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#E0E0E0"));
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(800);
        subtitle.setAlignment(Pos.CENTER);

        titleBox.getChildren().addAll(mainTitle, subtitle);
        return titleBox;
    }

    private GridPane createFeatureGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(40, 20, 60, 20));

        VBox feature1 = createFeatureBox("Adaptive Quizzing", "Our delivery options for your full team");
        VBox feature2 = createFeatureBox("Track Progress", "Welcome you on a 100 percent of the market");
        VBox feature3 = createFeatureBox("Personalized Learning", "Create your latest version in search for your own use...");
        grid.addRow(0, feature1, feature2, feature3);

        return grid;
    }

    private VBox createFeatureBox(String title, String description) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(25, 20, 25, 20));
        box.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );
        box.setPrefWidth(300);
        box.setPrefHeight(200);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.WHITE);

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 16));
        descLabel.setTextFill(Color.web("#E0E0E0"));
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setMaxWidth(280);

        box.getChildren().addAll(titleLabel, descLabel);
        return box;
    }

    private VBox createRoleSelection() {
        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);

        Label prompt = new Label("Ready for the manual manual?");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        prompt.setTextFill(Color.WHITE);

        HBox buttonBox = new HBox(40);
        buttonBox.setAlignment(Pos.CENTER);

        Button participantBtn = createRoleButton("Participant", "#4CAF50");
        participantBtn.setOnAction(e -> openUserLogin());

        Button adminBtn = createRoleButton("Administrator", "#FF5722");
        adminBtn.setOnAction(e -> openAdminLogin());

        buttonBox.getChildren().addAll(participantBtn, adminBtn);
        container.getChildren().addAll(prompt, buttonBox);

        return container;
    }

    private Button createRoleButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(220);
        button.setPrefHeight(50);
        button.setStyle(
            "-fx-background-radius: 25;" +
            "-fx-background-color: " + color + ";" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        // Hover effect
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

    private void openUserLogin() {
        try {
            UserLogin userLogin = new UserLogin();
            Stage userStage = new Stage();
            userLogin.start(userStage);
            userStage.setMaximized(true); // Keep user login window maximized
            primaryStage.hide();
            userStage.setOnCloseRequest(event -> primaryStage.show());
        } catch (Exception ex) {
            showErrorAlert("Error opening User Login: " + ex.getMessage());
        }
    }
    private void openAdminLogin() {
        try {
            AdminLogin adminLogin = new AdminLogin();
            Stage adminStage = new Stage();
            adminLogin.start(adminStage);
            adminStage.setMaximized(true); // Keep admin login window maximized
            primaryStage.hide();
            adminStage.setOnCloseRequest(event -> primaryStage.show());
        } catch (Exception ex) {
            showErrorAlert("Error opening Admin Login: " + ex.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}