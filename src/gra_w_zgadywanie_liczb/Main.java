package gra_w_zgadywanie_liczb;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        Random rand = new Random();
        int numToGuess = rand.nextInt(101);

        System.out.println("Zgadnij liczbę");
        Scanner sc = new Scanner(System.in);
        int num = -100;

        while(num!=numToGuess){
            while(!sc.hasNextInt()){
                sc.next();
                System.out.println("To nie jest liczba");
            }
            num = sc.nextInt();
            if (num<numToGuess){
                System.out.println("Za mało");
            } else if (num>numToGuess){
                System.out.println("Za dużo");
            } else {
                System.out.println("Zgadłeś!");
            }
        }
    }

}
