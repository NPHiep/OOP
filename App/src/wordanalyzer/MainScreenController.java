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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import utils.Controller;
import utils.HighScoreItem;
import utils.WordItem;
import utils.parsers.CHMParser;
import utils.text.TextTool;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author PhiHiep
 */
public class MainScreenController extends Controller {

    private boolean isDataLoaded = false;
    @FXML
    private TextField filepath;
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
    private Button openbutton;
    @FXML
    private Button tutorialbutton;
    @FXML
    private Button minibutton;
    @FXML
    private AnchorPane root;
    NotificationPane noti;
    private String file = "";
    private Tooltip tooltip = new Tooltip();

    TableColumn wordCol = new TableColumn("Word");
    TableColumn countCol = new TableColumn("Count");
    TableColumn nameCol = new TableColumn("Name");
    TableColumn scoreCol = new TableColumn("Score");

    @FXML
    private void handleOpenButtonAction(ActionEvent event) {

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(WordAnalyzer.mainStage);
        if (file != null) {
            filepath.setText(file.getPath());
        }
    }

    @FXML
    public void closeButtonAction(ActionEvent event) {
        try {
            //Save new leader board table
            writeScoreBoard("score.dat", WordAnalyzer.hiscoreData);
        } catch (IOException ex) {
            System.err.println("Cant write file");
        }

        // exit program
        Platform.exit();
    }

    private void writeScoreBoard(String filename, ObservableList<HighScoreItem> hiScore) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < hiScore.size(); i++) {
            outputWriter.write(hiScore.get(i).getName());
            outputWriter.newLine();
            outputWriter.write("" + hiScore.get(i).getScore());
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }

    @FXML
    private void miniButtonAction(ActionEvent event) {
        stage.setIconified(true);
    }

    @FXML
    private void gameButtonAction(ActionEvent event) throws IOException {
        if (WordAnalyzer.unsualKeyList.size() > 100) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
            Parent root = (Parent) loader.load();
            GameScreenController controller = loader.getController();
            Stage stage = new Stage();
            controller.initGame(stage);
            Scene scene = new Scene(root);
            stage.setTitle("Word Game");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setX(this.stage.getX());
            stage.setY(this.stage.getY());
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else if (WordAnalyzer.unsualKeyList.size() > 0) {
            // case of few of data
            noti.setText(WordAnalyzer.data.size() + " unsual words is not enough to make a wonderful game!");
            noti.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("../imgs/explain.png"))));
            noti.getStyleClass().removeAll("alert", "explain");
            noti.getStyleClass().add("explain");
            noti.show();
        } else {
            noti.setText("No data! Please browse a file to analyze and get data");
            noti.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("../imgs/explain.png"))));
            noti.getStyleClass().removeAll("alert", "explain");
            noti.getStyleClass().add("explain");
            noti.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // init notificationPane
        noti = new NotificationPane();
        Label label = new Label();
        label.setMinWidth(600);
        label.setMaxWidth(600);
        label.setWrapText(true);
        noti.setContent(label);
        noti.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(""))));
        noti.setShowFromTop(false);
        noti.setOpacity(.7);
        noti.getStyleClass().add("front-layer");
        noti.setOnShown(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(9), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (noti.isShowing()) {
                            noti.hide();
                        }
                    }
                }));
                timeline.play();
            }
        });
        root.getChildren().add(noti);

        // init tooltip for button
        openbutton.setTooltip(new Tooltip("Browser file"));
        gamebutton.setTooltip(new Tooltip("Create a test"));
        tutorialbutton.setTooltip(new Tooltip("Tutorial"));
        minibutton.setTooltip(new Tooltip("Minimize"));
        closebutton.setTooltip(new Tooltip("Close program"));

    }

    public void init(Stage stage) {

        this.stage = stage;

        // set auto search in searchtextfield
        searchtextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    ObservableList<WordItem> list = FXCollections.observableArrayList();
                    for (WordItem word : WordAnalyzer.data) {
                        if (word.getWord().startsWith(newValue)) {
                            list.add(word);
                        }
                    }
                    table.setItems(list);
                }
            }
        });

        // set drag and drop ability
        filepath.setOnDragOver(new EventHandler<DragEvent>() {
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
        filepath.setOnDragDropped(new EventHandler<DragEvent>() {
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

        // set filepath property
        filepath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (newVal.compareTo(file) != 0) {
                    if (processtext.getText().compareTo("Analyze") != 0) {
                        if (processtext.textProperty().isBound()) {
                            processtext.textProperty().unbind();
                        }
                        processtext.setText("Analyze");
                    }
                }
            }
        });

        // set tableview
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
                final WordItem item = (WordItem) newVal;
                StringBuilder builder = new StringBuilder();
                builder.append("Word: " + item.getWord() + "\n");
                builder.append("Frequency: " + item.getCount() + " time(s)\n");
                String key = WordAnalyzer.baseData.getBasicWordFromDict(item.getWord());
                builder.append("Base word: " + key);
                List<String> words = WordAnalyzer.staticTable.getMoreWord(key);
                if (words.size() > 1)
                    builder.append("\nNear words:\n");
                for (String s : words) {
                    if (s.compareTo(item.getWord()) != 0) {
                        builder.append(" - " + s + "\n");
                    }
                }
                tooltip.setText(builder.toString());
                tooltip.setAutoHide(true);
                tooltip.setConsumeAutoHidingEvents(false);
                tooltip.show(table, table.getScene().getWindow().getX() + table.getScene().getWidth(), table.getScene().getWindow().getY() + table.getScene().getHeight() / 3);
            }
        });

        // set leader board view
        //leader board table init data
        nameCol.setCellValueFactory(new PropertyValueFactory<HighScoreItem, String>("Name"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<HighScoreItem, String>("Score"));
        nameCol.setSortable(false);
        scoreCol.setSortable(false);
        leaderboardtable.setItems(WordAnalyzer.hiscoreData);
        leaderboardtable.getColumns().addAll(nameCol, scoreCol);

        //processing text
        processtext.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                if (s2.compareTo("Analyze") == 0) {
                    if (!processtext.getStyleClass().contains("clickable")) {
                        processtext.getStyleClass().add("clickable");
                    }
                } else {
                    if (processtext.getStyleClass().contains("clickable")) {
                        processtext.getStyleClass().remove("clickable");
                    }
                }
            }
        });
        processtext.getStyleClass().add("clickable");
    }

    @FXML
    private void analyzeClicked(MouseEvent event) {
        Text t = (Text) event.getSource();
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
                long start = System.currentTimeMillis();
                String message = null;
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
                        message = "Invalid file! We only support pdf and chm file.";
                        isValidFile = false;
                    }
                } catch (IOException e) {
                    updateMessage("Invalid file!");
                    message = "Invalid file path! Please check path and file permission.";
                    isValidFile = false;
                } catch (SAXException e) {
                    updateMessage("Error when parse file!");
                    message = "Parser didnt work correctly. Please restart and try again.";
                } catch (TikaException e) {
                    updateMessage("Error when parser file!");
                    message = "Parser didnt work correctly. Please restart and try again.";
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

                    System.out.println("Add completion");
                    //add words to table
                    WordAnalyzer.data.clear();
                    WordAnalyzer.staticTable.getListViewData();
                    System.out.println("UpdateList Success");

                    if (table.getItems() != WordAnalyzer.data)
                        table.setItems(WordAnalyzer.data);
                    WordAnalyzer.unsualKeyList = WordAnalyzer.staticTable.getUnsualWord(WordAnalyzer.baseData);

                    updateMessage("Finish!");
                    message = "Analyze successfull! Got " + WordAnalyzer.data.size() + " words in " + String.valueOf((System.currentTimeMillis() - start) / 1000.0) + "s";
                }

                final String mess = message;
                final boolean valid = isValidFile;

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (mess != null) {
                            if (valid) {
                                noti.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("../imgs/save_success.png"))));
                                noti.getStyleClass().removeAll("alert", "explain");
                            } else {
                                noti.setGraphic(new ImageView((new Image(getClass().getResourceAsStream("../imgs/save_fail.png")))));
                                noti.getStyleClass().removeAll("alert", "explain");
                                noti.getStyleClass().add("alert");
                            }
                            noti.setText(mess);
                            noti.show();
                        }
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

    public void tutorialButtonAction(ActionEvent actionEvent) {
        noti.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("../imgs/save_fail.png"))));
        noti.show("123");
    }

}
