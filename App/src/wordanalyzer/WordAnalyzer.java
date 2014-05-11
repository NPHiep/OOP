/*
 * handle main view and action
 */
package wordanalyzer;

import analyzer.BaseDict;
import analyzer.StaticTable;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.HighScoreItem;
import utils.WordItem;
import utils.text.TextTool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author PhiHiep
 */
public class WordAnalyzer extends Application {

    public static ArrayList<String> unsualKeyList = new ArrayList<String>();
    public static BaseDict baseData = new BaseDict();
    public static StaticTable staticTable = new StaticTable();
    public static final ObservableList<WordItem> data = FXCollections.observableArrayList();
    public static SimpleDoubleProperty progressValue = new SimpleDoubleProperty();
    public static TextTool text;
    public static Stage mainStage;
    public static List<WordItem> unsualWordList = new ArrayList<WordItem>();
    public static final ObservableList<HighScoreItem> hiscoreData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        Parent root = (Parent)loader.load();
        MainScreenController main = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        main.init();
        stage.show();

        //load hiscore table
        HighScoreItem hiScore;
        Scanner fileScanner = new Scanner(new File("score.dat"));
        while (fileScanner.hasNext()){
            String name = fileScanner.nextLine();
            int score = Integer.parseInt(fileScanner.nextLine());
            hiScore = new HighScoreItem(name, score);
            hiscoreData.add(hiScore);
        }
    }

    private String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
            }
        }

        return stringBuffer.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
