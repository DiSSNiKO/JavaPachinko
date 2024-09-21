package com.example.javafxapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class multiApplication extends Application {
    private int currentApp;
    @Override
    public void start(Stage primaryStage) throws IOException {
        double windowWidth = 800;
        double windowHeight = 600;
        currentApp = 0;

        //dynamicPane will display the app, bottom will have buttons for navigation
        VBox mainPane = new VBox();
        Pane dynamicPane = new Pane();
        dynamicPane.setMinHeight(500);
        dynamicPane.setMinWidth(800);
        generatesGraphics[] apps = { new BeanMachineAnimatic(windowWidth,windowHeight),new ClockPane(200,Color.PINK)};
        int appCount = apps.length; //For now only two apps are present, functional clock and bean machine animation


        mainPane.getChildren().add(dynamicPane);
        apps[currentApp].generateGraphics(dynamicPane);

        HBox navPane = new HBox();
        Button prevBtn = new Button("Previous program");
        Button nextBtn = new Button("Next program");
        prevBtn.setOnMouseClicked(e -> {
            if (currentApp == 0){
                currentApp = appCount-1;
            } else {
                currentApp--;
            }
            apps[currentApp].generateGraphics(dynamicPane);
        });
        nextBtn.setOnMouseClicked(e -> {
            if (currentApp == appCount-1){
                currentApp = 0;
            } else {
                currentApp++;
            }
            apps[currentApp].generateGraphics(dynamicPane);
        });
        navPane.getChildren().add(prevBtn);
        navPane.getChildren().add(nextBtn);
        mainPane.getChildren().add(navPane);
        navPane.setAlignment(Pos.BOTTOM_CENTER);
        navPane.setPadding(new Insets(30,0,30,0));
        navPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY,Insets.EMPTY)));
        Scene scene = new Scene(mainPane, windowWidth,windowHeight);
        primaryStage.setTitle("Program collection"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

    }

    public static void main(String[] args) {
        launch();
    }

}