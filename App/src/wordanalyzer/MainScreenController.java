/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordanalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.HighScoreItem;
import utils.WordItem;

/**
 *
 * @author PhiHiep
 */
public class MainScreenController implements Initializable {

    public static Stage gameStage;
    @FXML
    private TableView table;
    @FXML
    private TableView leaderboardtable;
    @FXML
    private Button closebutton;
    @FXML
    private TextField searchtextfield;
    @FXML
    public Text processtext;
    @FXML
    public static ProgressIndicator processindicator;
    @FXML
    public static Button gamebutton;
    @FXML
    private Button minibutton;

    @FXML
    private void handleOpenButtonAction(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(WordAnalyzer.mainStage);
        if (file != null) {
            WordAnalyzer.filePath.set(file.getPath());
        }
    }

    @FXML
    private void closeButtonAction(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) closebutton.getScene().getWindow();
        try {
            //Save new leader board table
            writeScoreBoard("score.dat", WordAnalyzer.hiscoreData);
        } catch (IOException ex) {
            System.err.println("Không ghi được file thống kê điểm");
        }
        // do what you have to do
        stage.close();
    }

    private void writeScoreBoard(String filename, ObservableList<HighScoreItem> hiScore) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < hiScore.size(); i++) {
            outputWriter.write(hiScore.get(i).getName()  + " " + hiScore.get(i).getScore());
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }

    @FXML
    private void miniButtonAction(ActionEvent event) {
        Stage stage = (Stage) minibutton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void gameButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
        Stage stage = new Stage();
        gameStage = stage;
        Scene scene = new Scene(root);
        stage.setTitle("Word Game");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }
    TableColumn wordCol = new TableColumn("Word");
    TableColumn countCol = new TableColumn("Count");
    TableColumn nameCol = new TableColumn("Name");
    TableColumn scoreCol = new TableColumn("Score");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchtextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            }
        });
        //list parsed words table init data
        wordCol.setCellValueFactory(
                new PropertyValueFactory<WordItem, String>("Word")
        );
        countCol.setCellValueFactory(
                new PropertyValueFactory<WordItem, String>("Count")
        );
        table.setItems(WordAnalyzer.data);
        table.getColumns().addAll(wordCol, countCol);
        //leader board table init data
        nameCol.setCellValueFactory(new PropertyValueFactory<HighScoreItem, String>("Name"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<HighScoreItem, String>("Score"));
        leaderboardtable.setItems(WordAnalyzer.hiscoreData);
        leaderboardtable.getColumns().addAll(nameCol, scoreCol);
        //processing text
        processtext.textProperty().bind(WordAnalyzer.processName);

    }

    @FXML
    private void analyzeButtonAction(ActionEvent event) {
        if (WordAnalyzer.filePath.get() != "") {
            wordanalyzer.WordAnalyzer.LoadFileData(WordAnalyzer.filePath.get());
        }
    }
}
