package oop8.utils;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by eleven on 5/12/14.
 */
public class ScoreDialogController extends Controller {

    @FXML
    private Text scoreText;
    @FXML
    private Text rankText;
    @FXML
    private TextField nameField;
    @FXML
    private ImageView medalImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void init(Stage stage, int score, int rank) {
        this.stage = stage;
        scoreText.setText("Score: " + score);
        rankText.setText("Rank: " + rank);
        switch (rank) {
            case 1:
                medalImage.setImage(new Image(getClass().getResourceAsStream("../imgs/master_medal.png")));
                break;  // master - super special
            case 2:
                medalImage.setImage(new Image(getClass().getResourceAsStream("../imgs/diamond_medal.png")));
                break; // special
            case 3:
                medalImage.setImage(new Image(getClass().getResourceAsStream("../imgs/gold_medal.png")));
                break; // 1st
            case 4:
                medalImage.setImage(new Image(getClass().getResourceAsStream("../imgs/silver_medal.png")));
                break; // 2nd
            case 5:
                medalImage.setImage(new Image(getClass().getResourceAsStream("../imgs/common_medal.png")));
                break; // 3rd
            case 6: case 7:case 8:
                medalImage.setImage(new Image(getClass().getResourceAsStream("../imgs/common_medal.png")));
                break; // other
        }
    }

    public String getName() {
        return nameField.getText();
    }

    public void closeDialog() {
        stage.close();
    }
}
