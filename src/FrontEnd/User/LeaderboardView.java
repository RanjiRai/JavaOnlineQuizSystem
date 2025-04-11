package FrontEnd.User;

import BackEnd.User.LeaderBoardController;
import Models.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardView {
    private Stage stage;
    private String username;

    public LeaderboardView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void start() {
        BorderPane root = new BorderPane();

        // Matching Main application's gradient background
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        // Main content container
        VBox mainContent = new VBox(40);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(60, 50, 50, 50));
        mainContent.setMaxWidth(1000);

        // Title matching Main app style
        Label title = new Label("Leaderboard");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        title.setTextFill(Color.WHITE);

        // Subtitle for consistency
        Label subtitle = new Label("Top performers in the quiz platform");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#E0E0E0"));

        // Table container
        VBox tableBox = createTableBox();
        
        // Back button matching Main app's button style
        Button backButton = createStyledButton("Back to Dashboard", "#4CAF50");
        backButton.setOnAction(e -> new UserDashboard(stage, username).start());

        mainContent.getChildren().addAll(title, subtitle, tableBox, backButton);
        root.setCenter(mainContent);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new UserDashboard(stage, username).start();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Leaderboard");
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createTableBox() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25, 20, 25, 20));
        box.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" + "-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" + "-fx-border-radius: 12;" + "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );
        box.setPrefWidth(900);

        TableView<Score> table = createStyledTable();
        List<Score> scores = LeaderBoardController.getTopScores();
        ObservableList<Score> data = FXCollections.observableArrayList(scores);
        table.setItems(data);

        box.getChildren().add(table);
        return box;
    }

    private TableView<Score> createStyledTable() {
        TableView<Score> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-family: Arial;"
        );
        
        Label placeholder = new Label("No scores available");
        placeholder.setFont(Font.font("Arial", 16));
        placeholder.setTextFill(Color.web("#E0E0E0"));
        table.setPlaceholder(placeholder);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String headerStyle = 
            "-fx-background-color: rgba(255,255,255,0.1);" + "-fx-text-fill: white;" + "-fx-font-family: Arial;" +
            "-fx-font-weight: bold;" + "-fx-font-size: 16;" + "-fx-alignment: center;" + "-fx-padding: 10;";

        String cellStyle = 
            "-fx-text-fill: #E0E0E0;" +"-fx-font-family: Arial;" +"-fx-font-size: 14;" +
            "-fx-alignment: center;" + "-fx-background-color: transparent;" + "-fx-padding: 8;";

        TableColumn<Score, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setStyle(headerStyle);
        usernameCol.setCellFactory(col -> new TableCell<Score, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(cellStyle);
            }
        });

        TableColumn<Score, Integer> quizIdCol = new TableColumn<>("Quiz ID");
        quizIdCol.setCellValueFactory(new PropertyValueFactory<>("quizId"));
        quizIdCol.setStyle(headerStyle);
        quizIdCol.setCellFactory(col -> new TableCell<Score, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item != null ? item.toString() : null);
                setStyle(cellStyle);
            }
        });

        TableColumn<Score, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreCol.setStyle(headerStyle);
        scoreCol.setCellFactory(col -> new TableCell<Score, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item != null ? item.toString() : null);
                setStyle(cellStyle);
            }
        });

        TableColumn<Score, String> timeCol = new TableColumn<>("Time Taken");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));
        timeCol.setStyle(headerStyle);
        timeCol.setCellFactory(col -> new TableCell<Score, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(cellStyle);
            }
        });
        table.getColumns().addAll(usernameCol, quizIdCol, scoreCol, timeCol);
        table.setPrefHeight(500);
        return table;
    }
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(220);
        button.setPrefHeight(50);
        button.setStyle(
            "-fx-background-radius: 25;" +"-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-radius: 25;" +  "-fx-background-color: derive(" + color + ", -15%);" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        ));
        return button;
    }
}