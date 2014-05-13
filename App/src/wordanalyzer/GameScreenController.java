/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordanalyzer;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.Controller;
import utils.HighScoreItem;
import utils.ScoreDialogController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author PhiHiep
 */
public class GameScreenController extends Controller {

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
    private AnchorPane root_attach;
    @FXML
    private FlowPane main_pane;

    private ArrayList<String> unusedList = new ArrayList<String>(WordAnalyzer.unsualKeyList);
    private ArrayList<String> sentenceList = new ArrayList<String>();
    private int currentPoint = 0;
    private String newWord;
    private int trueAnwser = 0;

    private static final int LIMIT_HIGHSCORE = 8;
    private Button[] btns;
    private ImageView imgFalse;
    private ImageView imgTrue;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initGame(Stage stage) {
        this.stage = stage;

        imgFalse = new ImageView(new Image(getClass().getResourceAsStream("../imgs/false_mark.png")));
        imgFalse.setMouseTransparent(true);
        imgFalse.setFitWidth(30);
        imgFalse.setFitHeight(30);

        imgTrue = new ImageView(new Image(getClass().getResourceAsStream("../imgs/true_mark.png")));
        imgTrue.setMouseTransparent(true);
        imgTrue.setFitWidth(30);
        imgTrue.setFitHeight(30);

        btns = new Button[]{answer1_button, answer2_button, answer3_button, answer4_button, answer5_button};

        nextQuestion();
    }

    private void nextQuestion() {
        // set to default
        question_text.setOpacity(0);
        for (Button btn: btns) btn.setOpacity(0);

        // init
        final Random rd = new Random();
        trueAnwser = rd.nextInt(5) + 1;
        sentenceList = new ArrayList<String>();

        if (unusedList.size() > 0) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    try {
                        String[] sentences = null;
                        do {
                            newWord = unusedList.get(rd.nextInt(unusedList.size()));
                            sentences = WordAnalyzer.text.getSentence(newWord);
                            if (sentences != null && sentences.length > 0) break;
                            else unusedList.remove(newWord);
                        } while (true);
                        unusedList.remove(newWord);
                        currentPoint++;
                        MakeRandom(trueAnwser, newWord, sentences[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            };

            (new Thread(task)).start();
        } else {
            gameFinish();
        }
    }

    private void MakeRandom(int correctAnswer, final String newWord, String rightSentence) {
        // show question_text
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                question_text.setText(newWord);
                score_text.setText("Score: " + (currentPoint - 1));
                showQuiz(1.5, question_text);
            }
        });

        System.out.println("Answer:" + correctAnswer);
        sentenceList.add(0, rightSentence);
        rightSentence = replaceWordInSenetece(rightSentence, newWord);

        // create wrong scentence
        final ArrayList<String> sents = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            String sent = wrongSentence();
            if (sent != null)
                sents.add(i, sent);
        }
        sents.add(correctAnswer - 1, rightSentence);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < btns.length; i++){
                    btns[i].setText(sents.get(i));
                }

                showQuiz(.75, answer1_button, answer2_button, answer3_button, answer4_button, answer5_button);
            }
        });
    }

    public void gameFinish() {
        //Real point
        currentPoint--;
        //If high score, update data
        if (currentPoint > 0 && isHighScore(currentPoint)) {
            int index = 0;
            while (index < WordAnalyzer.hiscoreData.size() && currentPoint <= WordAnalyzer.hiscoreData.get(index).getScore()) {
                index++;
            }
            if (index < LIMIT_HIGHSCORE) {
                String newName = "";

                try {
                    final Stage dlg = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../utils/Dialog.fxml"));
                    final Parent root = (Parent) loader.load();
                    ScoreDialogController controller = loader.getController();
                    controller.init(dlg, currentPoint, index + 1);

                        dlg.initOwner(stage);
                        dlg.initStyle(StageStyle.UNDECORATED);
                        dlg.initModality(Modality.WINDOW_MODAL);
                        final Scene scene = new Scene(root);
                            dlg.setScene(scene);
                            dlg.showAndWait();
                    newName = controller.getName();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (newName.compareTo("") == 0) {
                    newName = "Noname";
                }
                WordAnalyzer.hiscoreData.add(index, new HighScoreItem(newName, currentPoint));
                if (WordAnalyzer.hiscoreData.size() > LIMIT_HIGHSCORE) {
                    WordAnalyzer.hiscoreData.remove(LIMIT_HIGHSCORE);
                }
            }
        }
        //close game scene and return to main scene
        stage.close();
        WordAnalyzer.mainStage.show();
    }

    public void answerButtonAction(ActionEvent event) {
        if (checkLoading()) {
            Button source = (Button) event.getSource();
            if (btns[trueAnwser-1] == source){
                showTrue(source);
            } else {
                showFalse(source);
            }
        }
    }

    private void showQuiz(double timeInSecond, Node... nodes) {
        SequentialTransition sequentialTransition = SequentialTransitionBuilder.create().build();
        for (Node node : nodes) {
            double old = node.getOpacity();
            double dest;
            if (old == 0) dest = 0.7;
            else dest = 0;
            FadeTransition fadeTransition = FadeTransitionBuilder.create()
                    .duration(Duration.seconds(timeInSecond))
                    .node(node)
                    .fromValue(old)
                    .toValue(dest)
                    .build();
            sequentialTransition.getChildren().add(fadeTransition);
        }

        sequentialTransition.play();
    }

    private String replaceWordInSenetece(String sentence, String word) {
        String s = sentence.toLowerCase().replaceFirst("\\b" + word + "\\b", "[&]");
        int index = s.indexOf("[&]");
        if (index == -1) return null;
        return sentence.substring(0, index) + "[.........]" + sentence.substring(index + word.length());

    }

    private String wrongSentence() {
        Random rd = new Random();
        String fakeWord = "";
        do {
            int index = rd.nextInt(WordAnalyzer.unsualKeyList.size());
            fakeWord = WordAnalyzer.unsualKeyList.get(index);
            if (fakeWord.compareTo(newWord) == 0) continue;
            String[] sentences = WordAnalyzer.text.getSentence(fakeWord);
            if (sentences != null && sentences.length > 0) {
                // check if wrong sentence is same as true sentence
                for (String sentenceToCheck : sentences) {
                    boolean check = true;
                    for (String s : sentenceList)
                        if (sentenceToCheck.compareTo(s) == 0) {
                            check = false;
                        }
                    if (check) {
                        sentenceList.add(sentenceToCheck);
                        return replaceWordInSenetece(sentenceToCheck, fakeWord);
                    }
                }
            }
        } while (true);
    }

    private boolean isHighScore(int score) {
        if (WordAnalyzer.hiscoreData.size() < 8) {
            return true;
        }
        int index = WordAnalyzer.hiscoreData.size() - 1;
        if (score >= WordAnalyzer.hiscoreData.get(index).getScore()) {
            return true;
        }
        return false;
    }

    private void showTrue(Button node) {
        double w = node.getBoundsInParent().getWidth();
        double x = node.getLayoutX();
        double y = node.getLayoutY() + main_pane.getLayoutY();

        imgTrue.setLayoutX(x + w - 20);
        imgTrue.setLayoutY(y - 15);
        imgTrue.setOpacity(0);
       root_attach.getChildren().add(imgTrue);

       showQuiz(.75, imgTrue);
      for (Button btn : btns) {
          if (btn != node)
                showQuiz(1.5, btn);
        }

      Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root_attach.getChildren().clear();
              nextQuestion();
            }
        }));
      timeline.play();
    }

  private void showFalse(Button node) {
        double w = node.getBoundsInParent().getWidth();
        double x = node.getLayoutX();
        double y = node.getLayoutY() + main_pane.getLayoutY();

        imgFalse.setLayoutX(x + w - 30);
        imgFalse.setLayoutY(y - 15);
        imgFalse.setOpacity(0);

        w = btns[trueAnwser-1].getBoundsInParent().getWidth();
        x = btns[trueAnwser-1].getLayoutX();
        y = btns[trueAnwser-1].getLayoutY() + main_pane.getLayoutY();

      imgTrue.setLayoutX(x + w - 30);
      imgTrue.setLayoutY(y - 15);
        imgTrue.setOpacity(0);
       root_attach.getChildren().addAll(imgFalse, imgTrue);

        showQuiz(.75, imgFalse, imgTrue);
        for (int i = 0; i < btns.length; i++) {
            if (btns[i] !=node && (i+1) != trueAnwser)
               showQuiz(.75, btns[i]);
        }

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameFinish();
                    }
                });
            }
        }));
       timeline.play();
    }

    private boolean checkLoading() {
                return (answer1_button.getOpacity() == .7 && answer2_button.getOpacity() == .7 && answer3_button.getOpacity() == .7 && answer4_button.getOpacity() == .7 && answer5_button.getOpacity() == .7);
    }
}
