package gra_w_zgadywanie_liczb_2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Pomyśl liczbę z zakresu od 1 do 1000, a ja zgadne ją w 10 ruchach");
        int min = 0;
        int max = 1000;
        int guess = guessing(min, max);

        String answer = yesNo();

        while (!answer.equals("Y")) {
            System.out.println("Za dużo? Y/N");
            answer = yesNo();
            if (answer.equals("Y")) {
                max = guess;
            } else {
                System.out.println("Za malo? Y/N");
                answer = yesNo();
                if (answer.equals("Y")) {
                    min = guess;
                } else {
                    System.out.println("Nie oszukuj");
                }
            }
            guess = guessing(min, max);
            answer = yesNo();
        }
        System.out.println("Wygralem");
    }

    static int guessing(int min, int max) {
        int num = (max - min) / 2 + min;
        System.out.println("Czy to " + num + "? Y/N");
        return num;
    }

    static String yesNo() {

        Scanner yn = new Scanner(System.in);
        String str = yn.nextLine();

        while (!str.equals("Y") && !str.equals("N")) {
            System.out.println("Wymagane znaki to Y lub N");
            str = yn.nextLine();
        }
        return str;
    }
}




