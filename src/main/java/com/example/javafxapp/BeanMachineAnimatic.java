package com.example.javafxapp;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class BeanMachineAnimatic extends Pane implements generatesGraphics{
    final int MAX_BALLS = 30;
    final int MIN_BALLS = 1;
    final int MAX_BOUNCES = 10;
    final int MIN_BOUNCES = 2;
    final double startY;
    private double initialArcY;

    private Text[] counters;


    final double startX;

    private double ballSize = 8;

    public double getBallSize() {
        return ballSize;
    }

    private double obstacleDistance = getBallSize()*7;
    private double obstacleDistanceY = getBallSize()*4;

    private int ballCount;
    private ArrayList<Circle> beans = new ArrayList();
    private ArrayList<BeanArcTransition> beanAnimations = new ArrayList();

    public int getBallCount() {
        return ballCount;
    }

    private int bounces;
    private Button increaseBalls = new Button("More balls");
    private Button decreaseBalls = new Button("Less balls");
    private Button increaseChambers = new Button("More bounces");
    private Button decreaseChambers = new Button("Less bounces");
    private Button playAnim = new Button("Run animation");
    private Button stopAllAnims = new Button("Stop all");

    private Label chambersLabel = new Label("Bounces: ");
    private Label chambersCountLabel = new Label();
    private Label ballsLabel = new Label("Balls: ");
    private Label ballsCountLabel = new Label();
    private Label moneys = new Label("1000");
    private Label moneysLabel = new Label("Money \uD83E\uDD11:");
    private Button resetCash = new Button("Reset funds");
    private BigDecimal[][] multipliers = new BigDecimal[9][];

    private HBox functionalityBar = new HBox();

    private Pane beanMachineContainer = new Pane();


    public BeanMachineAnimatic(double parentWidth, double parentHeight){
        this.ballCount = MIN_BALLS;
        this.bounces = MIN_BOUNCES;
        this.setHeight(parentHeight);
        this.setWidth(parentWidth);
        this.startX = parentWidth/2;
        //moneyLabel
        this.getChildren().add(moneysLabel);
        this.getChildren().add(moneys);
        this.getChildren().add(resetCash);
        moneys.setLayoutX(startX-startX*0.8+25);
        moneys.setLayoutY(parentHeight/5);

        moneysLabel.setLayoutX(startX-startX*0.8 - 70);
        moneysLabel.setLayoutY(parentHeight/5);
        moneysLabel.setStyle("-fx-text-fill: green; -fx-font-size: 20px;");
        moneys.setStyle("-fx-text-fill: green; -fx-font-size: 20px;");

        resetCash.setLayoutX(startX+startX*0.8-20);
        resetCash.setLayoutY(parentHeight/3);
        this.startY = this.ballSize*2+10;
        multipliersSetup();
        populateMultipliers();
        initialArcY = startY+38+obstacleDistanceY;
        this.getChildren().add(beanMachineContainer);
        this.generateFunctionality();
    }
    private void multipliersSetup(){
        multipliers[0] = new BigDecimal[4];
        multipliers[1] = new BigDecimal[5];
        multipliers[2] = new BigDecimal[6];
        multipliers[3] = new BigDecimal[7];
        multipliers[4] = new BigDecimal[8];
        multipliers[5] = new BigDecimal[9];
        multipliers[6] = new BigDecimal[10];
        multipliers[7] = new BigDecimal[11];
        multipliers[8] = new BigDecimal[12];
    }
    private void populateMultipliers(){
        BigDecimal difference = new BigDecimal(0.5);
        BigDecimal multiplier;
        BigDecimal[] currentMult;
        for (int i = 0; i<multipliers.length;i++){
            currentMult = multipliers[i];
            multiplier = new BigDecimal(0.5);
            if(currentMult.length%2==0){
                currentMult[currentMult.length/2] =  multiplier;
                currentMult[(currentMult.length/2)-1] =  multiplier;
                int startIndx = 0;
                int endIndx = currentMult.length-1;
                int differences = (currentMult.length/2);
                for (int j = 0; j<currentMult.length/2 - 1;j++){
                    currentMult[startIndx] = multiplier.add(difference.multiply(new BigDecimal(differences)));
                    currentMult[endIndx] = multiplier.add(difference.multiply(new BigDecimal(differences)));
                    startIndx++;
                    endIndx--;
                    differences--;
                }
            } else {
                currentMult[currentMult.length/2] =  multiplier;
                int startIndx = 0;
                int endIndx = currentMult.length-1;
                int differences = (currentMult.length/2);
                for (int j = 0; j<currentMult.length/2;j++){
                    currentMult[startIndx] = multiplier.add(difference.multiply(new BigDecimal(differences)));
                    currentMult[endIndx] = multiplier.add(difference.multiply(new BigDecimal(differences)));
                    startIndx++;
                    endIndx--;
                    differences--;
                }
            }
        }
        strictifyLoss();
    }
    public void strictifyLoss(){
        BigDecimal[] currentMult;
        for (int i = 0; i<multipliers.length;i++){
            currentMult = multipliers[i];
            for (int j = 0; j<currentMult.length;j++){
                if (currentMult[j].equals(new BigDecimal(0.5))){
                    currentMult[j] = new BigDecimal(0.25).setScale(2, RoundingMode.DOWN);
                }
            }
        }
    }
    private BigDecimal[] selectCorrectMultipliers(int chambers){
        for (int i = 0; i < multipliers.length; i++){
            if(multipliers[i].length==chambers){
                return multipliers[i];
            }
        }
        return new BigDecimal[1];
    }

    @Override
    public void generateGraphics(Pane parentPane) {
        parentPane.getChildren().clear();
        updateMachine();
        parentPane.getChildren().add(this);
    }
    private void updateMachine(){
        beanMachineContainer.getChildren().clear();
        generateObstacles();
    }

    private void generateObstacles(){
        double initialX;
        double initialY = startY+50;
        double xOffset = 0;
        for(int i = 1; i<bounces+2;i++){
            initialX = startX-xOffset;
            for (int j = 0; j<i; j++){
                Circle obstacleCircle = new Circle(getBallSize()/2,Color.CRIMSON);
                obstacleCircle.setCenterY(initialY);
                obstacleCircle.setCenterX(initialX);
                beanMachineContainer.getChildren().add(obstacleCircle);
                initialX+=obstacleDistance;
            }
            xOffset+=obstacleDistance/2;
            initialY+=getBallSize()*4;
        }
        xOffset-=obstacleDistance/2;
        generateChambers(startX-xOffset,xOffset,initialY+25,obstacleDistance);
    }

    private void generateChambers(double initialX, double xOffset, double yCoord, double obstacleDistance) {
        stopAnimations();
        Line floor = new Line(initialX-obstacleDistance, yCoord, (initialX + xOffset * 2)+obstacleDistance, yCoord);
        beanMachineContainer.getChildren().add(floor);
        for (double i = initialX-obstacleDistance; i <= (initialX + xOffset * 2)+obstacleDistance; i += obstacleDistance) {
            Line spoke = new Line(i, yCoord, i, yCoord - 20);
            beanMachineContainer.getChildren().add(spoke);
        }
        ballsCountLabel.setText(String.valueOf(ballCount));
        chambersCountLabel.setText(String.valueOf(bounces));
        counters = new Text[bounces + 2]; //There are always 2 more chambers than the number of bounces that occur
        double initialXForCounter = initialX-obstacleDistance/2;
        BigDecimal[] neededMultipliers = selectCorrectMultipliers(counters.length);
        for (int i = 0; i<counters.length; i++){
            counters[i] = new Text("x"+neededMultipliers[i].toString());
            counters[i].setLayoutX(initialXForCounter);
            counters[i].setLayoutY(yCoord+20);
            beanMachineContainer.getChildren().add(counters[i]);
            initialXForCounter+=obstacleDistance;
        }
    }
    private void generateBallsAndRunAnimation(){
        stopAnimations();
        BeanMachineSimulation results = new BeanMachineSimulation(bounces,ballCount);
        beans.clear();
        beanAnimations.clear();
        for(int i = 0; i<ballCount;i++){
            beans.add(new Circle(ballSize, new Color(Math.random(),Math.random(),Math.random(),1)));
            beans.get(i).setCenterX(startX);
            beans.get(i).setCenterY(startY+38);
            beanAnimations.add(new BeanArcTransition(obstacleDistance/2,obstacleDistanceY,startX,
                    startY+38+obstacleDistanceY,obstacleDistance/2,obstacleDistanceY,
                    0.3,results.getDirections().get(i),beans.get(i),this, moneys,selectCorrectMultipliers(counters.length),results.getChamberIndices().get(i)));
            this.getChildren().add(beans.get(i));
        }
        final int[] anims = {0};
        playAnim.setDisable(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.25), event -> {
            if (anims[0] <ballCount){
                beanAnimations.get(anims[0]).playAnimation();
                anims[0]++;
            } else {
                playAnim.setDisable(false);
            }
        }));
        timeline.setCycleCount(ballCount+1); // Run the timeline for the number of balls plus one more to handle animation end
        timeline.play();
    }



    private void generateFunctionality(){
        functionalityBar.getChildren().addAll(playAnim,stopAllAnims,increaseBalls,decreaseBalls,decreaseChambers,
                increaseChambers,chambersLabel,chambersCountLabel,ballsLabel,ballsCountLabel);
        functionalityBar.setAlignment(Pos.CENTER);
        HBox.setMargin(playAnim,new Insets(10,10,20,10));
        HBox.setMargin(stopAllAnims,new Insets(10,10,20,10));
        HBox.setMargin(decreaseBalls,new Insets(10,10,20,10));
        HBox.setMargin(increaseBalls,new Insets(10,10,20,10));
        HBox.setMargin(decreaseChambers,new Insets(10,10,20,10));
        HBox.setMargin(increaseChambers,new Insets(10,10,20,10));
        HBox.setMargin(chambersLabel,new Insets(10,10,20,10));
        HBox.setMargin(chambersCountLabel,new Insets(10,20,20,5));
        HBox.setMargin(ballsLabel,new Insets(10,20,20,20));
        HBox.setMargin(ballsCountLabel,new Insets(10,20,20,5));

        this.getChildren().add(functionalityBar);
        increaseBalls.setOnMouseClicked(e -> {
            ballCount = generalIncreaseHandler(ballCount,MAX_BALLS,MIN_BALLS);
            ballsCountLabel.setText(String.valueOf(ballCount));
            updateMachine();
        });
        decreaseBalls.setOnMouseClicked(e -> {
            ballCount = generalDecreaseHandler(ballCount,MAX_BALLS,MIN_BALLS);
            ballsCountLabel.setText(String.valueOf(ballCount));
            updateMachine();
        });
        increaseChambers.setOnMouseClicked(e -> {
            bounces = generalIncreaseHandler(bounces,MAX_BOUNCES,MIN_BOUNCES);
            chambersCountLabel.setText(String.valueOf(bounces));
            updateMachine();

        });
        decreaseChambers.setOnMouseClicked(e -> {
            bounces = generalDecreaseHandler(bounces,MAX_BOUNCES,MIN_BOUNCES);
            chambersCountLabel.setText(String.valueOf(bounces));
            updateMachine();
        });

        playAnim.setOnMouseClicked(e -> {

            generateBallsAndRunAnimation();
        });
        resetCash.setOnMouseClicked(e -> {
            moneys.setText("1000");
        });
        stopAllAnims.setOnMouseClicked(e -> {
            stopAnimations();
        });

    }

    public void stopAnimations(){
        for (BeanArcTransition anim :
                beanAnimations) {
            anim.getBeanTransition().stop();
        }
        beanAnimations.clear();
        for(int i = 0; i<beans.size();i++){
            this.getChildren().remove(beans.get(i));
        }
        beans.clear();
    }
    private int generalIncreaseHandler( int countVariable, int countVariableMax, int countVariableMin){
            if(countVariable<countVariableMax){
                return countVariable+1;
            } else {
                return countVariableMin;
            }
    }
    private int generalDecreaseHandler( int countVariable, int countVariableMax,int countVariableMin){
        if(countVariable>countVariableMin){
            return countVariable-1;
        } else {
            return countVariableMax;
        }
    }
}
