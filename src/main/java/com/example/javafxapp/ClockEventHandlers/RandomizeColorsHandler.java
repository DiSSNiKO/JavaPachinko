package com.example.javafxapp.ClockEventHandlers;

import com.example.javafxapp.ClockPane;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class RandomizeColorsHandler implements EventHandler {
    ScrollPane scrollPane;
    VBox clockBox;
    double clockRadius;
    public RandomizeColorsHandler(VBox clockBox, ScrollPane scrollPane, double clockRadius){
        this.scrollPane = scrollPane;
        this.clockBox = clockBox;
        this.clockRadius = clockRadius;
    }
    @Override
    public void handle(Event event) {
        scrollPane.setContent(ClockPane.randomizeClockColors(clockBox.getChildren().size(),clockRadius));
    }
}
