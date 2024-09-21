package com.example.javafxapp;

import java.util.ArrayList;


//Simulates the outcome for a given number of beans,
//with a specific number of bounces

public class BeanMachineSimulation {


    private int bounces;
    private int ballNum;
    //An index of directions directly correlates to an index of chamberIndices
    private ArrayList<String> directions = new ArrayList<>();
    private ArrayList<Integer> chamberIndices = new ArrayList<>(); //Stores the index of the chamber where a ball ends up in

    public ArrayList<Integer> getChamberIndices() {
        return chamberIndices;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public int getBounces(){
        return bounces;
    }
    public int getBallNum(){
        return ballNum;
    }
    BeanMachineSimulation(int bounces, int ballNum){
        this.bounces = bounces;
        this.ballNum = ballNum;
        this.generateResults();
    }

    private void generateResults(){
        String directionsTaken;
        int randomDirection;
        double ballLocation;
        for (int i = 0; i<ballNum;i++){
            directionsTaken="";
            ballLocation = (bounces+2)/2; //Initial ball location is horizontally centered to the floor of the machine, bounces plus 2 correlates with the amount of chambers because my bounces amount is off tf
            for (int j = 0; j<=bounces;j++){
                randomDirection = (int) (Math.random() * 2);
                if (randomDirection==0){ //0 for left, 1 for right
                    directionsTaken+="L";
                    ballLocation-=0.5;
                } else {
                    directionsTaken+="R";
                    ballLocation+=0.5;
                }
            }
            directions.add(directionsTaken);
            chamberIndices.add((int)ballLocation);
        }
    }
}
