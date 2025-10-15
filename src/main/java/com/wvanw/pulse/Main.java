package com.wvanw.pulse;

import com.wvanw.pulse.core.TickManager;
import com.wvanw.pulse.graphics.RenderManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();

        new TickManager(gc).start();
    }


    public static void main(String[] args) {
        launch();
    }
}