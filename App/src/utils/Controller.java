package utils;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Created by eleven on 5/12/14.
 */
public abstract  class Controller implements Initializable {
    private double mouseDragOffsetX;
    private double mouseDragOffsetY;
    protected Stage stage;

    public void pressPane(MouseEvent event){
        mouseDragOffsetX = event.getSceneX();
        mouseDragOffsetY = event.getSceneY();
    }

    public void dragWindow(MouseEvent event){
        stage.setX(event.getScreenX() - mouseDragOffsetX);
        stage.setY(event.getScreenY() - mouseDragOffsetY);
    }
}
