package org.example;

import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

class User {
    private String name;
    private int age;
    ArrayList<Integer> scores = new ArrayList<Integer>();

    void setName(String name){
        this.name = name;
    }
    void setAge(int age){
        this.age = age;
    }
    void setScore(Integer score){
        scores.add(score);
    }

    String getName(){
        return this.name;
    }
    int getAge(){
        return this.age;
    }
    void printScores(){
        for(Integer scr: scores){
            System.out.print(scr + " ");
        }
        System.out.println();
    }
}

class Game {
    ArrayList<ArrayList<Integer>> gameGrid = new ArrayList<ArrayList<Integer>>();
    private int gridLen = 7;
    ArrayList<Integer> completed = new ArrayList<Integer>();

    void initialiseGrid(){
        gameGrid.addAll(Collections.nCopies(gridLen,
                new ArrayList<Integer>(Collections.nCopies(gridLen, 0))));
    }

    void placeShip(int shipNo){
        boolean isValid = false;
        while(!isValid){
            boolean isHorz = (int)(Math.random()*2) < 1;
            int row = (int)(Math.random() * gridLen);
            int col = (int)(Math.random() * gridLen);

            if(isHorz && isFreeHorzSpace(row, col)){
                placeHorizontal(row, col, shipNo);
                isValid = true;
            }
            if(!isHorz && isFreeVertSpace(row, col)){
                placeVertical(row, col, shipNo);
                isValid = true;
            }

        }
    }

    boolean isFreeHorzSpace(int row, int col){
        return col < gridLen-3 &&
                gameGrid.get(row).get(col) == 0 &&
                gameGrid.get(row).get(col+1) == 0 &&
                gameGrid.get(row).get(col+2) == 0;
    }

    boolean isFreeVertSpace(int row, int col){
        return row < gridLen-3 &&
                gameGrid.get(row).get(col) == 0 &&
                gameGrid.get(row+1).get(col) == 0 &&
                gameGrid.get(row+2).get(col) == 0;
    }

    void placeHorizontal(int row, int col, int shipNo){
        for(int i=0; i<3; i++){
            gameGrid.get(row).set(col+i, shipNo);
        }
        viewGrid();
    }

    void placeVertical(int row, int col, int shipNo){
        for(int i=0; i<3; i++){
            gameGrid.get(row+i).set(col, shipNo);
        }
        viewGrid();
    }

    void setupGame(){
        initialiseGrid();

        //Randomly assign three dot coms
        for(int i=1; i<2; i++){
            placeShip(i);
        }
        completed.clear();

        completed.addAll(Collections.nCopies(3, 0));
    }

    void viewGrid(){
        System.out.print("    ");
        for(int i=0; i<gridLen; i++){
            System.out.print(i + " ");
        }
        System.out.println("\n");

        for(int i=0; i<gridLen; i++){
            System.out.print((char)(65 + i) + " : ");
            for(int j=0; j<gridLen; j++){
                System.out.print(gameGrid.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    void playGame(User user){
        System.out.print("\nHello, " + user.getName() + ".How are you feeling today?\n");
        System.out.println("Welcome to the SinkADotCom Game. Here we go...\n");

        Scanner myObj = new Scanner(System.in);

        int cmp = 0, hit = 0, miss = 0;
        while(cmp < 3){ //Keep playing...
            viewGrid();
            System.out.print("Make a guess(ex. A3, B5, etc.): ");
            String guess = myObj.nextLine().toUpperCase();

            if(guess.length() != 2){
                System.out.println("Invalid input.. Please try again...");
                continue;
            }

            int row = (char)(guess.charAt(0))-'A', col = (char)(guess.charAt(1))-'0';

            if(row >= gridLen || col >= gridLen){
                System.out.println("Invalid guess... Please try again.");
                continue;
            }

            if(gameGrid.get(row).get(col) == 0){
                System.out.println("Miss");
                miss++;
            } else{
                hit++;
                completed.set(gameGrid.get(row).get(col)-1, completed.get(gameGrid.get(row).get(col)-1)+1);

                if(completed.get(gameGrid.get(row).get(col)-1) == 3){
                    cmp++;
                    System.out.println("Ouch! You sunk a dot com.  :(");
                    System.out.println("Kill\n");
                } else{
                    System.out.println("Hit");
                }
                gameGrid.get(row).set(col, 0);
            }

        }

        System.out.println("Game over!\n");
        Integer score = (int)(((hit*1.00)/(miss+hit))*10);
        System.out.println("Your score is: " + score);
        user.setScore(score);
        setupGame();
    }
}

public class Main {
    public static void main(String[] args){
        Game g1 = new Game();
        g1.setupGame();
        Scanner sc = new Scanner(System.in);

        User john = new User();
        String name = "";
        while(name.isEmpty()){
            System.out.print("Enter your Name: ");
            name = sc.nextLine();

            if(name.isEmpty()){
                System.out.println("Enter something dude...");
            }
        }

        System.out.print("Enter your Age: ");
        int age = sc.nextInt();

        john.setName(name);
        john.setAge(age);

        String playAgain;
        do {
            g1.playGame(john);
            System.out.print("Here are your last scores: ");
            john.printScores();

            System.out.print("Do you want to play again? (y/n): ");
            playAgain = sc.next();
        } while(playAgain.equalsIgnoreCase("y"));

    }
}