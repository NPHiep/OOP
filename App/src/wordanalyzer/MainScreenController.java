/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordanalyzer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.controlsfx.control.NotificationPane;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import utils.HighScoreItem;
import utils.WordItem;
import utils.parsers.CHMParser;
import utils.text.TextTool;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author PhiHiep
 */
public class MainScreenController implements Initializable {

    public static Stage gameStage;
    private boolean isDataLoaded = false;
    @FXML private TextField filepath;
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
    private Button gamebutton;
    @FXML
    private Button minibutton;
    @FXML private Button openbutton;
    @FXML private AnchorPane root;
    NotificationPane noti;
    private String file = "";
    private Tooltip tooltip = new Tooltip();

    @FXML
    private void handleOpenButtonAction(ActionEvent event) {

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(WordAnalyzer.mainStage);
        if (file != null) {
            filepath.setText(file.getPath());
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
            System.err.println("Cant write file");
        }
        // do what you have to do
        stage.close();
    }

    private void writeScoreBoard(String filename, ObservableList<HighScoreItem> hiScore) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < hiScore.size(); i++) {
            outputWriter.write(hiScore.get(i).getName());
            outputWriter.newLine();
            outputWriter.write(""+hiScore.get(i).getScore());
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
        if (WordAnalyzer.data.size() > 0) {
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
    }
    TableColumn wordCol = new TableColumn("Word");
    TableColumn countCol = new TableColumn("Count");
    TableColumn nameCol = new TableColumn("Name");
    TableColumn scoreCol = new TableColumn("Score");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noti = new NotificationPane();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinWidth(600);
        anchorPane.setPrefWidth(600);
        noti.setContent(anchorPane);
        noti.setShowFromTop(false);
        noti.setOnShown(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (noti.isShowing()){
                            noti.hide();
                        }
                    }
                }));
                timeline.play();
            }
        });
        root.getChildren().add(noti);
    }

    public void init(){
        searchtextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    ObservableList<WordItem>list = FXCollections.observableArrayList();
                    for (WordItem word : WordAnalyzer.data) {
                        if (word.getWord().startsWith(newValue)){
                            list.add(word);
                        }
                    }
                    table.setItems(list);
                }
            }
        });

        // set drag and drop ability
        filepath.getScene().setOnDragOver(new EventHandler<DragEvent>() {
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
        filepath.getScene().setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    File file = db.getFiles().get(0);
                    filepath.setText(file.getAbsolutePath());
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        filepath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (newVal.compareTo(file) != 0){
                    if (!processtext.getStyleClass().contains("clickable")){
                        if (processtext.textProperty().isBound()){
                            processtext.textProperty().unbind();
                        }
                        processtext.getStyleClass().add("clickable");
                        processtext.setText("Analyze");
                    }
                }
            }
        });
        //list parsed words table init data
        wordCol.setCellValueFactory(
                new PropertyValueFactory<WordItem, String>("Word")
        );
        countCol.setCellValueFactory(
                new PropertyValueFactory<WordItem, String>("Count")
        );
        table.getColumns().addAll(wordCol, countCol);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldVal, Object newVal) {
                if (newVal == null) return;
                final WordItem item = (WordItem)newVal;
                StringBuilder builder = new StringBuilder();
                builder.append("Word: "+item.getWord()+"\n");
                builder.append("Frequency: "+item.getCount()+" time(s)\n");
                String key = WordAnalyzer.baseData.getBasicWordFromDict(item.getWord());
                builder.append("Base word: "+key);
                List<String> words = WordAnalyzer.staticTable.getMoreWord(key);
                if (words.size() > 1)
                    builder.append("\nNear words:\n");
                for (String s: words){
                    if (s.compareTo(item.getWord()) != 0){
                        builder.append(" - "+s+"\n");
                    }
                }
                tooltip.setText(builder.toString());
                tooltip.setAutoHide(true);
                tooltip.show(table, table.getScene().getWindow().getX() + table.getScene().getWidth(), table.getScene().getWindow().getY()+table.getScene().getHeight()/3);
            }
        });
        //leader board table init data
        nameCol.setCellValueFactory(new PropertyValueFactory<HighScoreItem, String>("Name"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<HighScoreItem, String>("Score"));
        leaderboardtable.setItems(WordAnalyzer.hiscoreData);
        leaderboardtable.getColumns().addAll(nameCol, scoreCol);
        //processing text
        processtext.getStyleClass().add("clickable");
    }

    @FXML
    private void analyzeClicked(MouseEvent event)
    {
        Text t = (Text)event.getSource();
        if (t.getStyleClass().contains("clickable"))
            if (filepath.getText() != "") {
                loadFileData(filepath.getText());
            }
    }

    public void loadFileData(final String filePath) {
        gamebutton.setDisable(true);
        filepath.setDisable(true);
        searchtextfield.setDisable(true);
        openbutton.setDisable(true);
        table.setDisable(true);
        if (processtext.getStyleClass().contains("clickable")) processtext.getStyleClass().remove("clickable");
        //reset binding status of display message
        if (processtext.textProperty().isBound()) {
            processtext.textProperty().unbind();
        }
        //Checking file name, file is valid if it is a PDF or CHM file.
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                updateMessage("Checking file...");
                //check valid file
                boolean isValidFile = true;
                //token for parsing process
                ContentHandler handler = new BodyContentHandler(-1);
                Metadata metadata = new Metadata();
                InputStream input = null;
                //parsing input file
                updateMessage("Parsing file...");
                    try {
                        input = new FileInputStream(filePath);
                        if (filePath.endsWith(".pdf")) {
                            (new PDFParser()).parse(input, handler, metadata, new ParseContext());
                        } else if (filePath.endsWith(".chm")) {
                            (new CHMParser(filePath)).parse(input, handler, metadata, new ParseContext());
                        } else {
                            updateMessage("Invalid File!");
                            isValidFile = false;
                        }
                } catch (IOException e) {
                        updateMessage("Invalid File!");
                        isValidFile = false;
                } catch (SAXException e) {
                        updateMessage("Invalid File!");
                        isValidFile = false;
                } catch (TikaException e) {
                        updateMessage("Invalid File!");
                        isValidFile = false;
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
                        file = filepath.getText();
                        updateMessage("Loading data for first time...");
                        WordAnalyzer.baseData.ImportData();
                        isDataLoaded = true;
                    }
                    // process data in file
                    updateMessage("Analyzing data...");
                    WordAnalyzer.staticTable.clearAllData();
                    WordAnalyzer.text = new TextTool(handler.toString());
                    // Parser text to word list
                    WordAnalyzer.text.addToDataBase();

                    //add words to table
                    WordAnalyzer.staticTable.getListViewData();

                    if (table.getItems() != WordAnalyzer.data)
                        table.setItems(WordAnalyzer.data);
                    WordAnalyzer.unsualKeyList = WordAnalyzer.staticTable.getUnsualWord(WordAnalyzer.baseData);

                    updateMessage("Finish!");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            noti.setText("Analyze successfull! Got "+WordAnalyzer.data.size()+" words.");
                            if (noti.isShowing()){
                                noti.hide();
                                noti.show();
                            } else {
                                noti.show();
                            }

                        }
                    });
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        openbutton.setDisable(false);
                        gamebutton.setDisable(false);
                        searchtextfield.setDisable(false);
                        filepath.setDisable(false);
                        table.setDisable(false);

                    }
                });

                return null;
            }
        };

        //call task
        new Thread(task).start();
        //bind task status to display messeage
        processtext.textProperty().bind(task.messageProperty());
    }
}
