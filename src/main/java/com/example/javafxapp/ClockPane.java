package com.example.javafxapp;

import com.example.javafxapp.ClockEventHandlers.RandomizeColorsHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ClockPane extends Pane implements generatesGraphics {
    private Line hourHand = new Line();
    private Line minuteHand = new Line();
    private Line secondHand = new Line();
    private final double clockRadius;
    private long hour = (System.currentTimeMillis()/1000/60/60%12); //GMT Timezone
    private long minute = (System.currentTimeMillis()/1000/60%60); //GMT Timezone
    private long second = (System.currentTimeMillis()/1000%60); //GMT Timezone
    private ObservableList chiList;

    EventHandler<ActionEvent> clockUpdateHandler = e -> {

        hour = (System.currentTimeMillis()/1000/60/60%12); //GMT Timezone
        minute = (System.currentTimeMillis()/1000/60%60); //GMT Timezone
        second = (System.currentTimeMillis()/1000%60); //GMT Timezone
        updateHands();
    };
    public ClockPane(double clockRadius, Color color){
        this.setPadding(new Insets(10,0,10,0));
        this.clockRadius = clockRadius;
        this.chiList = this.getChildren();
        chiList.add(new Circle(clockRadius,clockRadius,clockRadius, color));
        chiList.addAll(hourHand,minuteHand,secondHand);
        //Create a 1-second animation that will fire clockUpdateHandler
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000),clockUpdateHandler));
        timeline.setCycleCount(Animation.INDEFINITE);

        createTimeIndicators();
        updateHands();

        timeline.play();
    }

    public static ClockPane createNewClock(Color clockColor, double clockRadius){
        return new ClockPane(clockRadius,clockColor);
    }

    public static VBox randomizeClockColors(int size, double clRadius){
        VBox container = new VBox();
        container.setPadding(new Insets(10,20,10,10));
        for (int i = 0; i< size;i++){
            Node node = createNewClock(new Color(Math.random(),Math.random(),Math.random(),1),clRadius);
            container.getChildren().add(node);
        }
        return container;
    }
    private void createTimeIndicators(){
        double centerX = clockRadius, centerY = clockRadius;
        int hourMarker = 12;
        for (int i = 90;i<450;i+=90){
            Text hourText = new Text(String.valueOf(hourMarker));
            hourMarker-=3;
            hourText.setLayoutY(getHeight()-Math.sin(Math.toRadians(i))*clockRadius*0.85+clockRadius);
            hourText.setLayoutX(Math.cos(Math.toRadians(i))*clockRadius*0.85+clockRadius);
            chiList.add(hourText);
        }

        double startingAngle = 90;
        for (int i = (int)startingAngle;i<450;i+=30){
            Line hourLine = new Line(Math.cos(Math.toRadians(i))*clockRadius+centerX,
                    Math.sin(Math.toRadians(i))*clockRadius+centerY,
                    Math.cos(Math.toRadians(i))*(clockRadius*0.92)+centerX,
                    Math.sin(Math.toRadians(i))*(clockRadius*0.92)+centerY);
            chiList.add(hourLine);
        }
        for (int i = (int)startingAngle;i<450;i+=6){
            Line minSecLine = new Line(Math.cos(Math.toRadians(i))*clockRadius+centerX,
                    Math.sin(Math.toRadians(i))*clockRadius+centerY,
                    Math.cos(Math.toRadians(i))*(clockRadius*0.96)+centerX,
                    Math.sin(Math.toRadians(i))*(clockRadius*0.96)+centerY);
            chiList.add(minSecLine);
        }
    }
    private void updateHands(){
        double centerX = clockRadius, centerY = clockRadius;
        double startingAngle = 90;

        //Hour hand



        double preciseHour = 12-((double)hour+(double)minute/60);
        hourHand.setStartX(centerX);
        hourHand.setStartY(centerY);
        hourHand.setEndX(centerX+(clockRadius*Math.cos(Math.toRadians(startingAngle+preciseHour*30)))*0.3);
        hourHand.setEndY(centerY-(clockRadius*Math.sin(Math.toRadians(startingAngle+preciseHour*30)))*0.3);

        //Minute hand
        minute = 60 - minute;
        minuteHand.setStartX(centerX);
        minuteHand.setStartY(centerY);
        minuteHand.setEndX(centerX+(clockRadius*Math.cos(Math.toRadians(6*minute+startingAngle)))*0.6);
        minuteHand.setEndY(centerY-(clockRadius*Math.sin(Math.toRadians(6*minute+startingAngle)))*0.6);


        //Second hand

        second = (60 - second);
        secondHand.setStartX(centerX);
        secondHand.setStartY(centerY);
        secondHand.setEndX(centerX + (clockRadius*Math.cos(Math.toRadians(startingAngle+6*second)))*0.8);
        secondHand.setEndY(centerY - (clockRadius*Math.sin(Math.toRadians(startingAngle+6*second)))*0.8);
    }

    @Override
    public void generateGraphics(Pane parentPane){
        parentPane.getChildren().clear();

        VBox mainVBox = new VBox();
        VBox clockVBox = new VBox();
        ScrollPane scrollPane = new ScrollPane();


        Button addClock = new Button("Add clock");
        Button randomizeColors = new Button("Randomize clock colors");
        HBox clonktrols = new HBox(addClock,randomizeColors);
        HBox.setMargin(addClock,new Insets(5,5,5,5));
        HBox.setMargin(randomizeColors,new Insets(5,5,5,5));


        clonktrols.setAlignment(Pos.CENTER);

        clockVBox.getChildren().add(this);
        clockVBox.setPadding(new Insets(10,20,10,10));
        scrollPane.setPrefHeight(clockRadius*2);
        scrollPane.setContent(clockVBox);
        scrollPane.setFitToWidth(true);


        mainVBox.getChildren().addAll(scrollPane,clonktrols);

        randomizeColors.setOnMouseClicked(new RandomizeColorsHandler(clockVBox,scrollPane,clockRadius));
        addClock.setOnMouseClicked(e -> {
            clockVBox.getChildren().add(ClockPane.createNewClock(new Color(Math.random(),Math.random(),Math.random(),1),clockRadius));
            scrollPane.setContent(clockVBox);
        });
        parentPane.getChildren().add(mainVBox);
    }

}
