/*
 * handle main view and action
 */
package oop8.wordanalyzer;

import oop8.analyzer.BaseDict;
import oop8.analyzer.StaticTable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import oop8.utils.HighScoreItem;
import oop8.utils.WordItem;
import oop8.utils.text.TextTool;

import java.io.*;
import java.util.ArrayList;
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
    public static TextTool text;
    public static Stage mainStage;
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
        main.init(stage);
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

    public static void main(String[] args) {
        launch(args);
    }
}
