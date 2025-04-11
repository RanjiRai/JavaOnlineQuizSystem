package FrontEnd.Admin;

import Models.Question;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class ManageQuestions {
    private Stage stage;
    private int selectedQuizId;
    private ListView<String> questionListView;

    public void start(Stage stage, int quizId) {
        this.stage = stage;
        this.selectedQuizId = quizId;
        showManageQuestionsScreen();
        stage.setMaximized(true);
    }

    private void showManageQuestionsScreen() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
            new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))),
            CornerRadii.EMPTY, Insets.EMPTY)));

        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));

        Label titleLabel = new Label("MANAGE QUESTIONS");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Manage questions for the selected quiz");
        subtitleLabel.setFont(Font.font("Arial", 18));
        subtitleLabel.setTextFill(Color.web("#E0E0E0"));

        VBox questionManagementBox = createQuestionManagementBox();
        
        content.getChildren().addAll(titleLabel, subtitleLabel, questionManagementBox);
        root.setCenter(content);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new AdminDashboard().start(stage);
            }
        });

        stage.setScene(scene);
        stage.setTitle("Manage Questions");
        stage.show();
    }

    private VBox createQuestionManagementBox() {
        VBox formLayout = new VBox(20);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(30));
        formLayout.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" + "-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" +"-fx-border-radius: 12;" + "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 0, 4);"
        );
        formLayout.setMaxWidth(800);

        questionListView = new ListView<>();
        questionListView.setPrefHeight(400);
        questionListView.setStyle(
            "-fx-background-color:rgba(255, 255, 255, 0.8);"+"-fx-border-color: rgba(255,255,255,0.3);" + "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" + "-fx-background-radius: 8;" + "-fx-font-size: 14px;"
        );
        loadQuestions();

        Button addButton = createStyledButton("Add Question", "#4CAF50");
        Button deleteButton = createStyledButton("Delete Selected", "#F44336");
        Button backButton = createStyledButton("Back to Dashboard", "#2196F3");

        addButton.setOnAction(e -> showAddQuestionDialog());
        deleteButton.setOnAction(e -> deleteSelectedQuestion());
        backButton.setOnAction(e -> new AdminDashboard().start(stage));

        HBox buttonBox = new HBox(20, addButton, deleteButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        formLayout.getChildren().addAll(questionListView, buttonBox);
        return formLayout;
    }

    private void loadQuestions() {
        questionListView.getItems().clear();
        List<Question> questions = QuizDao.getQuestionsByQuizId(selectedQuizId);
        for (Question q : questions) {
            questionListView.getItems().add(q.getQuestionText());
        }
    }

    private void showAddQuestionDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Add Question");

        BorderPane dialogRoot = new BorderPane();
        dialogRoot.setBackground(new Background(new BackgroundFill(
            new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2761")),
                new Stop(1, Color.web("#408EC6"))),
            CornerRadii.EMPTY, Insets.EMPTY)));

        VBox dialogContent = new VBox(20);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(30));

        Label dialogTitle = new Label("ADD NEW QUESTION");
        dialogTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        dialogTitle.setTextFill(Color.WHITE);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        TextField questionField = createStyledTextField("Enter question");
        TextField optionAField = createStyledTextField("Option A");
        TextField optionBField = createStyledTextField("Option B");
        TextField optionCField = createStyledTextField("Option C");
        TextField optionDField = createStyledTextField("Option D");
        
        TextArea explanationField = new TextArea();
        explanationField.setPromptText("Enter explanation (optional)");
        explanationField.setPrefRowCount(3);
        explanationField.setStyle(
            "-fx-padding: 10;" + "-fx-background-radius: 8;" + "-fx-background-color: rgba(255,255,255,0.8);" +
            "-fx-border-color: rgba(255,255,255,0.3);" +"-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +   "-fx-font-size: 14px;" );

        ComboBox<String> correctOptionBox = new ComboBox<>();
        correctOptionBox.getItems().addAll("A", "B", "C", "D");
        correctOptionBox.setPromptText("Select correct answer");
        correctOptionBox.setStyle(
            "-fx-font-size: 14px;" + "-fx-background-radius: 8;" + "-fx-background-color: rgba(255,255,255,0.8);" +
            "-fx-border-color: rgba(255,255,255,0.3);" +  "-fx-border-width: 1;" +"-fx-border-radius: 8;" + "-fx-padding: 8;"
        );
        correctOptionBox.setPrefWidth(300);

        Button saveButton = createStyledButton("Save Question", "#4CAF50");
        saveButton.setOnAction(e -> saveQuestion(
            questionField, optionAField, optionBField, 
            optionCField, optionDField, explanationField, correctOptionBox, dialog
        ));

        Button cancelButton = createStyledButton("Cancel", "#F44336");
        cancelButton.setOnAction(e -> dialog.close());

        HBox dialogButtons = new HBox(20, saveButton, cancelButton);
        dialogButtons.setAlignment(Pos.CENTER);

        grid.add(new Label("Question:"), 0, 0);
        grid.add(questionField, 1, 0);
        grid.add(new Label("Option A:"), 0, 1);
        grid.add(optionAField, 1, 1);
        grid.add(new Label("Option B:"), 0, 2);
        grid.add(optionBField, 1, 2);
        grid.add(new Label("Option C:"), 0, 3);
        grid.add(optionCField, 1, 3);
        grid.add(new Label("Option D:"), 0, 4);
        grid.add(optionDField, 1, 4);
        grid.add(new Label("Correct Answer:"), 0, 5);
        grid.add(correctOptionBox, 1, 5);
        grid.add(new Label("Explanation:"), 0, 6);
        grid.add(explanationField, 1, 6);

        dialogContent.getChildren().addAll(dialogTitle, grid, dialogButtons);
        dialogRoot.setCenter(dialogContent);

        Scene dialogScene = new Scene(dialogRoot, 600, 700);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void saveQuestion(TextField questionField, TextField optionAField, 
                            TextField optionBField, TextField optionCField,
                            TextField optionDField, TextArea explanationField,
                            ComboBox<String> correctOptionBox, Stage dialog) {
        if (questionField.getText().trim().isEmpty() || 
            optionAField.getText().trim().isEmpty() || 
            optionBField.getText().trim().isEmpty() || 
            optionCField.getText().trim().isEmpty() || 
            optionDField.getText().trim().isEmpty() || 
            correctOptionBox.getValue() == null) {

            showAlert("Error", "All fields except explanation must be filled out!");
            return;
        }

        boolean success = QuizDao.addQuestion(selectedQuizId, 
                questionField.getText().trim(), optionAField.getText().trim(), 
                optionBField.getText().trim(), optionCField.getText().trim(), 
                optionDField.getText().trim(), correctOptionBox.getValue().trim(),
                explanationField.getText().trim());

        if (success) {
            loadQuestions();
            dialog.close();
        } else {
            showAlert("Error", "Failed to add question.");
        }
    }

    private void deleteSelectedQuestion() {
        int selectedIndex = questionListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            List<Question> questions = QuizDao.getQuestionsByQuizId(selectedQuizId);
            int questionId = questions.get(selectedIndex).getId();

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Question");
            confirmationAlert.setHeaderText("Are you sure you want to delete this question?");
            confirmationAlert.setContentText("This action cannot be undone.");
            
            // Style the alert dialog to match our theme
            DialogPane dialogPane = confirmationAlert.getDialogPane();
            dialogPane.setStyle(
                "-fx-background-color: linear-gradient(to right, #1e2761, #408EC6);" +
                "-fx-border-color: rgba(255,255,255,0.2);"
            );
            dialogPane.setHeaderText(null);
            dialogPane.lookup(".content.label").setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;"
            );
            
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean success = QuizDao.deleteQuestion(questionId);
                    if (success) {
                        loadQuestions();
                    } else {
                        showAlert("Error", "Failed to delete question.");
                    }
                }
            });
        } else {
            showAlert("Warning", "Please select a question to delete.");
        }
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(200);
        button.setPrefHeight(45);
        button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-radius: 25;" +"-fx-background-color: derive(" + color + ", -15%);" + "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0.2, 0, 4);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-radius: 25;" + "-fx-background-color: " + color + ";" +"-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.2, 0, 3);"
        ));

        return button;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(Font.font("Arial", 14));
        textField.setStyle(
            "-fx-padding: 10;" + "-fx-background-radius: 8;" +   "-fx-background-color: rgba(255,255,255,0.8);" +
            "-fx-border-color: rgba(255,255,255,0.3);" +"-fx-border-width: 1;" + "-fx-border-radius: 8;"
        );
        textField.setPrefWidth(400);
        return textField;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert dialog to match our theme
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: linear-gradient(to right, #1e2761, #408EC6);" +
            "-fx-border-color: rgba(255,255,255,0.2);"
        );
        dialogPane.lookup(".content.label").setStyle(
            "-fx-text-fill: white;" +  "-fx-font-size: 14px;" );
        
        alert.showAndWait();
    }
}