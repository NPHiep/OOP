package oop8.wordanalyzer;

/**
 * Created by eleven on 5/13/14.
 */
/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * A sample in which a node scales larger and smaller over a given time.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.ScaleTransition
 * @see javafx.animation.ScaleTransitionBuilder
 * @see javafx.animation.Transition
 */
public class Test extends Application {

    private ScaleTransition scaleTransition;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 150,150));
        ImageView rect = new ImageView(new Image(getClass().getResourceAsStream("../imgs/true_mark.png")));
        root.getChildren().add(rect);

    }

    public void play() {
        scaleTransition.play();
    }

    @Override public void stop() {
        scaleTransition.stop();
    }

    public double getSampleWidth() { return 150; }

    public double getSampleHeight() { return 150; }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }
    public static void main(String[] args) { launch(args); }
}
