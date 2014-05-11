/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordanalyzer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import utils.HighScoreItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author PhiHiep
 */
public class GameScreenController implements Initializable {

    @FXML
    private Button answer1_button;
    @FXML
    private Button answer2_button;
    @FXML
    private Button answer3_button;
    @FXML
    private Button answer4_button;
    @FXML
    private Button answer5_button;
    @FXML
    private Text question_text;
    @FXML
    private Text score_text;
    @FXML
    private Button closebutton;

    ArrayList<String> unusedList = new ArrayList<String>(WordAnalyzer.unsualKeyList);
    ArrayList<String> sentenceList = new ArrayList<String>();
    int currentPoint = 0;
    String newWord;
    int trueAnwser = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vector unsualList = WordAnalyzer.staticTable.getUnsualWord(WordAnalyzer.baseData);
        // unsualList.
        initGame();
    }

    public void anw1ButtonAction() {
        if (trueAnwser == 1) {
            nextQuestion();
        } else {
            gameFinish();
        }
    }

    public void awn2ButtonAction() {
        if (trueAnwser == 2) {
            nextQuestion();
        } else {
            gameFinish();
        }
    }

    public void awn3ButtonAction() {
        if (trueAnwser == 3) {
            nextQuestion();
        } else {
            gameFinish();
        }
    }

    public void awn4ButtonAction() {
        if (trueAnwser == 4) {
            nextQuestion();
        } else {
            gameFinish();
        }
    }

    public void awn5ButtonAction() {
        if (trueAnwser == 5) {
            nextQuestion();
        } else {
            gameFinish();
        }
    }

    private void initGame() {
        nextQuestion();
    }

    private void nextQuestion() {
        // init
        Random rd = new Random();
        trueAnwser = rd.nextInt(5) + 1;
        sentenceList = new ArrayList<String>();

        if (unusedList.size() > 0) {
            String[] sentences = null;
            do {
                newWord = unusedList.get(rd.nextInt(unusedList.size()));
                sentences = WordAnalyzer.text.getSentence(newWord);
                if (sentences != null && sentences.length > 0) break;
            } while(true);
            unusedList.remove(newWord);
            question_text.setText(newWord);
            score_text.setText("Score: " + currentPoint);
            currentPoint++;
            MakeRandom(trueAnwser, newWord, sentences[0]);
        } else {
            gameFinish();
        }
    }

    public void gameFinish() {
        //Real point
        currentPoint--;
        //If high score, update data
        if (currentPoint >0 && isHighScore(currentPoint)) {
            InputTextPrompt prompt = new InputTextPrompt(
                    MainScreenController.gameStage
            );
            String newName = prompt.getResult();
            if (newName == "") {
                newName = "Someone";
            }
            int index = 0;
                while (index < WordAnalyzer.hiscoreData.size() && currentPoint < WordAnalyzer.hiscoreData.get(index).getScore()) {
                    index++;
                }
            WordAnalyzer.hiscoreData.add(index, new HighScoreItem(newName, currentPoint));
            if (WordAnalyzer.hiscoreData.size() == 8) {
                WordAnalyzer.hiscoreData.remove(7);
            }
        }
        //close game scene and return to main scene
        MainScreenController.gameStage.close();
        WordAnalyzer.mainStage.show();

    }

    private void MakeRandom(int correctAnswer, String newWord, String rightSentence) {
        System.out.println("Answer:"+correctAnswer);
        sentenceList.add(0,rightSentence);
        rightSentence = replaceWordInSenetece(rightSentence, newWord);

        // create wrong scentence
        ArrayList<String> sents = new ArrayList<String>();
        for (int i = 0; i < 4; i++){
            sents.add(i,wrongSentence());
        }
        sents.add(correctAnswer-1, rightSentence);

        answer1_button.setText(sents.get(0));
        answer2_button.setText(sents.get(1));
        answer3_button.setText(sents.get(2));
        answer4_button.setText(sents.get(3));
        answer5_button.setText(sents.get(4));
    }

    private String replaceWordInSenetece(String sentence, String word) {
        return sentence.replaceFirst(word,"[.........]");
    }

    private String wrongSentence() {
        Random rd = new Random();
        String fakeWord = "";
        do {
            int index = rd.nextInt(WordAnalyzer.unsualKeyList.size());
            fakeWord = WordAnalyzer.unsualKeyList.get(index);
            if (fakeWord.compareTo(newWord) == 0) continue;
            String[] sentences = WordAnalyzer.text.getSentence(fakeWord);
            if (sentences != null && sentences.length > 0){
                // check if wrong sentence is same as true sentence
                for (String sentenceToCheck: sentences){
                    boolean check = true;
                    for (String s: sentenceList)
                    if (sentenceToCheck.compareTo(s) == 0){
                        check = false;
                    }
                    if (check){
                        sentenceList.add(sentenceToCheck);
                        return replaceWordInSenetece(sentenceToCheck, fakeWord);
                    }
                }
            }
        } while (true);
    }

    private boolean isHighScore(int score) {
        if (WordAnalyzer.hiscoreData.size() < 7) {
            return true;
        }
        int index = WordAnalyzer.hiscoreData.size() - 1;
        if (score >= WordAnalyzer.hiscoreData.get(index).getScore()) {
            return true;
        }
        return false;
    }
}

class InputTextPrompt {

    private final String result;

    public InputTextPrompt(Window owner) {
        final Stage dialog = new Stage();
        dialog.setTitle("Enter your name");
        dialog.initOwner(owner);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.WINDOW_MODAL);
        final TextField textField = new TextField();
        final Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                dialog.close();
            }
        });
        textField.setMinHeight(TextField.USE_PREF_SIZE);

        final VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER_RIGHT);
        layout.setStyle("-fx-background-color: azure; -fx-padding: 10;");
        layout.getChildren().setAll(
                textField,
                submitButton
        );

        dialog.setScene(new Scene(layout));
        dialog.showAndWait();

        result = textField.getText();

    }

    public String getResult() {
        return result;
    }
}
