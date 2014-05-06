/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordanalyzer;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;
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
import utils.WordItem;

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
    int unsualCount = WordAnalyzer.unsualKeyList.size();
    int[] flag = new int[unsualCount];
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
        for (int i = 0; i < unsualCount; i++) {
            flag[i] = i;
        }
        Random rd = new Random();
        for (int i = 0; i < unsualCount; i++) {
            int k = rd.nextInt(unsualCount);
            int tmp = flag[i];
            flag[i] = flag[k];
            flag[k] = tmp;
        }
        //question_text.setText(WordAnalyzer.data.get(flag[0]).getWord());
        nextQuestion();
    }

    private void nextQuestion() {
        Random rd = new Random();
        trueAnwser = rd.nextInt(5) + 1;
        if (currentPoint < unsualCount) {
            newWord = WordAnalyzer.unsualKeyList.get(flag[currentPoint]);
            question_text.setText(newWord);
            score_text.setText("Score: " + currentPoint);
            currentPoint++;
        } else {
            gameFinish();
        }
        MakeRandom(trueAnwser, newWord);
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
            while (currentPoint < WordAnalyzer.hiscoreData.get(index).getScore() && index < WordAnalyzer.hiscoreData.size()) {
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

    private void MakeRandom(int correctAnswer, String newWord) {
        System.out.println(""+newWord);
        String sentence = WordAnalyzer.text.getSentence(newWord)[0];
        sentence = sentence.toLowerCase();
        sentence = sentence.replaceFirst(newWord, "[........]");

        switch (correctAnswer) {
            case 1:
                answer1_button.setText(sentence);
                answer2_button.setText(wrongSentence(currentPoint, 1));
                answer3_button.setText(wrongSentence(currentPoint, 2));
                answer4_button.setText(wrongSentence(currentPoint, 3));
                answer5_button.setText(wrongSentence(currentPoint, 4));
                break;
            case 2:
                answer2_button.setText(sentence);
                answer1_button.setText(wrongSentence(currentPoint, 1));
                answer3_button.setText(wrongSentence(currentPoint, 2));
                answer4_button.setText(wrongSentence(currentPoint, 3));
                answer5_button.setText(wrongSentence(currentPoint, 4));

                break;
            case 3:
                answer3_button.setText(sentence);
                answer2_button.setText(wrongSentence(currentPoint, 1));
                answer1_button.setText(wrongSentence(currentPoint, 2));
                answer4_button.setText(wrongSentence(currentPoint, 3));
                answer5_button.setText(wrongSentence(currentPoint, 4));

                break;
            case 4:
                answer4_button.setText(sentence);
                answer2_button.setText(wrongSentence(currentPoint, 1));
                answer3_button.setText(wrongSentence(currentPoint, 2));
                answer1_button.setText(wrongSentence(currentPoint, 3));
                answer5_button.setText(wrongSentence(currentPoint, 4));

                break;
            case 5:
                answer5_button.setText(sentence);
                answer2_button.setText(wrongSentence(currentPoint, 1));
                answer3_button.setText(wrongSentence(currentPoint, 2));
                answer4_button.setText(wrongSentence(currentPoint, 3));
                answer1_button.setText(wrongSentence(currentPoint, 4));

                break;
            default:
        }

    }

    private String wrongSentence(int index, int distance) {
        String sentence;
        int trueIndex = flag[index];
        if ((trueIndex + distance) >= unsualCount) {
            trueIndex = Math.abs(trueIndex - distance);
        } else {
            trueIndex += distance;
        }
        String fakeWord = WordAnalyzer.unsualKeyList.get(trueIndex);
        sentence = WordAnalyzer.text.getSentence(fakeWord)[0];
        sentence = sentence.toLowerCase();
        fakeWord = fakeWord.toLowerCase();
        return sentence.replace(fakeWord, "[.........]");
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
