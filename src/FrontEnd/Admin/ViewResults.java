package FrontEnd.Admin;

import BackEnd.Database.DatabaseConnection;
import Models.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ViewResults {

    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
            new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))),
            CornerRadii.EMPTY, Insets.EMPTY)));

        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));

        Label titleLabel = new Label("Quiz Results");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);

        TableView<Score> tableView = createResultsTable();
        loadResults(tableView);

        Button backButton = createStyledButton("Back to Dashboard", "#FF5722");
        backButton.setOnAction(e -> new AdminDashboard().start(stage));

        VBox tableContainer = new VBox(20, tableView, backButton);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(30));
        tableContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +"-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" + "-fx-border-radius: 12;" + "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );

        content.getChildren().addAll(titleLabel, tableContainer);
        root.setCenter(content);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new AdminDashboard().start(stage);
            }
        });

        stage.setScene(scene);
        stage.setTitle("View Results");
        stage.setMaximized(true);
        stage.show();
    }

    private TableView<Score> createResultsTable() {
        TableView<Score> tableView = new TableView<>();
        tableView.setMaxWidth(900);
        tableView.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: transparent;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        TableColumn<Score, String> userColumn = new TableColumn<>("Username");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userColumn.setPrefWidth(200);

        TableColumn<Score, Integer> quizColumn = new TableColumn<>("Quiz ID");
        quizColumn.setCellValueFactory(new PropertyValueFactory<>("quizId"));
        quizColumn.setPrefWidth(100);

        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setPrefWidth(100);

        TableColumn<Score, String> timeColumn = new TableColumn<>("Time Taken");
        timeColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFormattedTime()));
        timeColumn.setPrefWidth(120);

        TableColumn<Score, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getTimestamp();
            if (timestamp != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        dateColumn.setPrefWidth(200);

        tableView.getColumns().addAll(userColumn, quizColumn, scoreColumn, timeColumn, dateColumn);
        return tableView;
    }

    private void loadResults(TableView<Score> tableView) {
        ObservableList<Score> resultsList = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT u.username, s.quiz_id, s.score, s.date, s.time_taken " +
                         "FROM scores s JOIN users u ON s.user_id = u.id ORDER BY s.score DESC, s.time_taken ASC";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                resultsList.add(new Score(
                    rs.getString("username"),
                    rs.getInt("score"),
                    rs.getInt("quiz_id"),
                    rs.getTimestamp("date"),
                    rs.getInt("time_taken")
                ));
            }
            tableView.setItems(resultsList);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load results: " + e.getMessage());
        }
    }
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        button.setStyle(
            "-fx-background-radius: 25;" +"-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: derive(" + color + ", -15%);" +"-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-radius: 25;" +"-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        ));

        return button;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
