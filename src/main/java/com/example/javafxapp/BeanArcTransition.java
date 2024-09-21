package com.example.javafxapp;

import javafx.animation.PathTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class BeanArcTransition {
    private double arcCenterX;
    private Pane parentPane;
    private double arcCenterY;
    private double animRadiusX;
    private double animRadiusY;
    private double horizontalChange;
    private int lastDirection;

    private double verticalChange;
    private double duration;
    private int currentDirection = 0;
    private final String directionString;
    private Arc animPath = new Arc();
    private PathTransition beanTransition;

    public PathTransition getBeanTransition() {
        return beanTransition;
    }


    public BeanArcTransition(double animRadiusX, double animRadiusY, double arcCenterX, double arcCenterY,
                             double horizontalChange, double verticalChange, double durationSecs, String directionString,
                             Circle targetBean, Pane parentPane, Label moneyLabel, BigDecimal[] multiplierArray, int chamberIndice){
        this.animRadiusX = animRadiusX;
        this.animRadiusY = animRadiusY;
        this.arcCenterX = arcCenterX;
        this.arcCenterY = arcCenterY;
        this.horizontalChange = horizontalChange;
        this.verticalChange = verticalChange;
        this.duration = durationSecs;
        this.directionString = directionString;
        this.parentPane = parentPane;
        animPath.setCenterX(arcCenterX);
        animPath.setCenterY(arcCenterY);
        animPath.setRadiusX(animRadiusX);
        animPath.setRadiusY(animRadiusY);
        animPath.setStartAngle(90);
        animPath.setLength(90*(directionString.charAt(currentDirection) == 'L' ? 1 : -1)); //Controls movement, left or right
        animPath.setType(ArcType.OPEN);
        beanTransition = new PathTransition(Duration.seconds(durationSecs),this.animPath,targetBean);
        System.out.println(directionString);
        beanTransition.setOnFinished(e -> {
            if(this.currentDirection<this.directionString.length()){
                this.animPath.setCenterY(this.animPath.getCenterY() + this.verticalChange);
                this.animPath.setCenterX(this.animPath.getCenterX() + (this.horizontalChange*(this.directionString.charAt(this.currentDirection-1) == 'L' ? -1 : 1)));
                this.animPath.setLength(90*(this.directionString.charAt(currentDirection) == 'L' ? 1 : -1));
                lastDirection = (this.directionString.charAt(currentDirection) == 'L' ? 1 : -1);
                this.currentDirection++;
                this.beanTransition.play();
            } else {
                System.out.println(multiplierArray[chamberIndice]+ " chosen mult " + chamberIndice);
                BigDecimal newNum = new BigDecimal(moneyLabel.getText()).multiply(new BigDecimal(multiplierArray[chamberIndice].toString())).setScale(2, RoundingMode.DOWN);
                moneyLabel.setText(newNum.toString());
                this.parentPane.getChildren().remove(targetBean);
            }
        });


    }
    public void playAnimation() {
        this.beanTransition.play();
        currentDirection++;
    }
}
