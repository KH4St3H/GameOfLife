package com.example.gameofdeath;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Game extends Application {
    GridPane board = new GridPane();
    private boolean paused = false;
    private int width = 250;
    private int height = 130;

    private double speed = 0.15;
    public boolean[][] mat;
    public boolean started = false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        board.setAlignment(Pos.CENTER);
//        board.setHgap(1);
//        board.setVgap(1);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel pixel = new Pixel(i, j);
                board.add(pixel, i, j);
                pixel.setOnMouseDragOver(e-> {
                    pixel.activate();
                });
                pixel.setOnMouseClicked(e -> {
                    int ii = pixel.j;
                    int jj = pixel.i;
                    started = false;
                    if(e.getButton() != MouseButton.SECONDARY){
                        getRealPixel(ii, jj).toggle();
                        return;
                    }
                    getRealPixel(ii, jj-1).activate();

                    getRealPixel(ii+1, jj).activate();
                    getRealPixel(ii+1, jj+1).activate();
                    getRealPixel(ii-1, jj+1).activate();
                    getRealPixel(ii, jj+1).activate();
                });
            }

        }
        board.setGridLinesVisible(true);

        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(speed),
                        event -> {
                            try {
                                updateWorld();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
//                            System.out.println("this is called every 5 seconds on UI thread");
                        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        Button pause = new Button("pause");
        pause.setOnMouseClicked(e -> {
            if(pause.getText().equals("pause")) {
                pause.setText("resume");
                paused = true;
            }
            else {
                paused = false;
                started = false;
                pause.setText("pause");
            }
        });

        Button clear = new Button("clear");
        clear.setOnMouseClicked(e -> {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    getRealPixel(j, i).deactivate();
                }
            }
        });

        VBox vBox = new VBox(board, pause, clear);
        pause.setAlignment(Pos.CENTER);

        Scene gameScene = new Scene(vBox, 1000, 1200);
        primaryStage.setWidth(1500);
        primaryStage.setHeight(800);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Glindomber");
        primaryStage.show();
    }

    public void updateWorld() throws InterruptedException {
        long start = System.currentTimeMillis();
        if(paused)
            return;
        if(!started) {
            started = true;
            mat = new boolean[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    mat[i][j] = getPixel(i, j);
                }
            }
        }
        boolean[][] new_mat = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                boolean current = getPixel(i, j);

//                ArrayList<Boolean> c = new ArrayList<>();
                int ANC = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        try {
                            if (mat[i + k][j + l])
                                ANC++;
                        } catch (Exception e){
                            continue;

                        }
                    }
                }
                if(current)
                    ANC--;
//                System.out.println(ANC);
//                c.add(getPixel(i + 1, j));
//                c.add(getPixel(i - 1, j));
//                c.add(getPixel(i, j + 1));
//                c.add(getPixel(i, j - 1));
//                c.add(getPixel(i + 1, j + 1));
//                c.add(getPixel(i - 1, j + 1));
//                c.add(getPixel(i + 1, j - 1));
//                c.add(getPixel(i - 1, j - 1));
                //Alive Neighbours Count
//                int ANC = 0;
//                for (boolean b : c) {
//                    if (b)
//                        ANC++;
//
//                }
                new_mat[i][j] = mat[i][j];
                if (!(current && (ANC == 2 || ANC == 3)))
                    new_mat[i][j] = false;
                if (!current && ANC == 3)
                    new_mat[i][j] = true;

            }

        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mat[i][j] = new_mat[i][j];
                Pixel pix = (Pixel) board.getChildren().get(j * height + i);
                if (new_mat[i][j])
                    pix.activate();
                else
                    pix.deactivate();
            }
        }
//        mat = new_mat;
        long diff = System.currentTimeMillis()-start;
        if(diff<100)
            Thread.sleep(100-diff);
    }

    public boolean getPixel(int i, int j) {
        if (i < 0 || i >= height || j < 0 || j >= width) {
            return false;
        }
        return ((Pixel) board.getChildren().get(j * height + i)).active;

    }
    public Pixel getRealPixel(int i, int j){
        return (Pixel) board.getChildren().get(j * height + i);
    }
}