/*
 * handle main view and action
 */
package wordanalyzer;

import analyzer.BaseDict;
import analyzer.StaticTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import utils.HighScoreItem;
import utils.WordItem;
import utils.parsers.CHMParser;
import utils.text.TextTool;

/**
 *
 * @author PhiHiep
 */
public class WordAnalyzer extends Application {

    public static SimpleStringProperty filePath = new SimpleStringProperty();
    public static SimpleStringProperty processName = new SimpleStringProperty();
    public static ArrayList<String> unsualKeyList = new ArrayList<String>();
    public static BaseDict baseData = new BaseDict();
    public static StaticTable staticTable = new StaticTable();
    public static final ObservableList<WordItem> data = FXCollections.observableArrayList();
    public static SimpleDoubleProperty progressValue = new SimpleDoubleProperty();
    public static TextTool text;
    private static boolean isDataLoaded = false;
    public static Stage mainStage;
    public static List<WordItem> unsualWordList = new ArrayList<WordItem>();
    public static final ObservableList<HighScoreItem> hiscoreData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root);
        //init scene status
        filePath.set("");
        processName.set("");
        MainScreenController.processindicator.setVisible(false);
        MainScreenController.gamebutton.setDisable(true);
        //Drag and Drop Function
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.LINK);
                } else {
                    event.consume();
                }
            }
        });
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    File file = db.getFiles().get(0);
                    filePath.set(file.getAbsolutePath());
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        //Get file path textbox 
        TextField filePathTextField = (TextField) scene.lookup("#filepath");
        //bind file path value to textbox
        filePathTextField.textProperty().bind(filePath);
        stage.setScene(scene);
        stage.show();

        //load hiscore table
        HighScoreItem hiScore;
        Scanner fileScanner = new Scanner(new File("score.dat"));
        while (fileScanner.hasNext()){
            System.out.println("==========================================aaa===");
            String name = fileScanner.next();
            int score = fileScanner.nextInt();
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

    public static void LoadFileData(final String filePath) {
        MainScreenController.processindicator.setVisible(true);
        MainScreenController.gamebutton.setDisable(true);
        //reset binding status of display message
        if (processName.isBound()) {
            processName.unbind();
        }
        //Checking file name, file is valid if it is a PDF or CHM file.
        processName.set("Checking file...");
        Task task = new Task<Void>() {
            @Override
            public Void call() {

                //check valid file
                boolean isValidFile = true;
                //token for parsing process
                ContentHandler handler = new BodyContentHandler(-1);
                Metadata metadata = new Metadata();
                InputStream input = null;
                //parsing input file
                updateMessage("Parsing file...");
                if (filePath.endsWith(".pdf")) {
                    try {
                        input = new FileInputStream(filePath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (filePath.endsWith(".pdf")) {
                        (new PDFParser()).parse(input, handler, metadata, new ParseContext());
                    } else if (filePath.endsWith(".chm")) {
                        (new CHMParser(filePath)).parse(input, handler, metadata, new ParseContext());
                    } else {
                        updateMessage("Invalid File!");
                        isValidFile = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (TikaException e) {
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                        }
                    }
                }
                if (isValidFile) {
                    //import base data to handle 
                    if (!isDataLoaded) {
                        updateMessage("Loading data for first time...");
                        baseData.ImportData();
                        isDataLoaded = true;
                    }
                    // process data in file
                    updateMessage("Analyzing data...");
                    text = new TextTool(handler.toString());
                    // Parser text to word list
                    text.addToDataBase();
                    //add words to table
                    staticTable.getListViewData();
                    updateProgress(1, 1);
                    unsualKeyList = staticTable.getUnsualWord(baseData);

                    updateMessage("Finish!");

                    MainScreenController.gamebutton.setVisible(true);
                    MainScreenController.gamebutton.setDisable(false);

                }
                MainScreenController.processindicator.setVisible(false);
                return null;
            }
        };
        //call task
        new Thread(task)
                .start();
        //bind task status to display messeage
        processName.bind(task.messageProperty());
    }
}
