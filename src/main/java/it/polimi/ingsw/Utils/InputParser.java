package it.polimi.ingsw.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputParser {

    private final Scanner input;


    public InputParser(){
        this.input = new Scanner(System.in);
    }

    public String readLine() {
        synchronized (input){
            return input.nextLine();
        }
    }

    public int readNumber() {
        boolean found = false;
        int number = 0;
        while(!found){
            String input = readLine();
            try {
                number = Integer.parseInt(input);
                found = true;
            }
            catch (NumberFormatException e){
                System.out.println("Invalid input - please input an integer.");
            }
        }
        return number;
    }

    public int readBoundNumber(int lo, int hi) {
        int number = readNumber();
        while(number < lo || number > hi){
            System.out.printf("The number must be between %d and %d%n", lo, hi);
            number = readNumber();
        }
        return number;
    }

    public int readNumberFromSelection(List<Integer> choices){
        int number = readNumber();
        while(!choices.contains(number)){
            System.out.println("The number must be one of the following: " + Arrays.toString(choices.toArray()));
            number = readNumber();
        }
        return number;
    }

    public String readLineFromSelection(List<String> choices){
        String line = readLine();
        while(!choices.contains(line)){
            System.out.println("The number must be one of the following: " + Arrays.toString(choices.toArray()));
            line = readLine();
        }
        return line;
    }



}