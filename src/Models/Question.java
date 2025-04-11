package Models;

public class Question {
    private int id;
    private String questionText;
    private String optionA, optionB, optionC, optionD;
    private String correctOption;
    private String explanation; // New field

    public Question(int id, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption, String explanation) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.explanation = explanation;
    }

    // Existing getters
    public int getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectOption() { return correctOption; }
    
    // New getter and setter for explanation
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}