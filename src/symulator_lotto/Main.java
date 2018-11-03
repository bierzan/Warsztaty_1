package symulator_lotto;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final int HOW_MANY_NUM = 6;
        
        lottoResult(lottoCoupon(HOW_MANY_NUM),lotteryNum(HOW_MANY_NUM));

    }

    static int[] lottoCoupon(int howManyNum) {
        Scanner sc = new Scanner(System.in);

        int[] coupon = new int[howManyNum];

        System.out.println("Podaj pierwszą liczbę do Twojego kuponu Lotto");

        while (howManyNum > 0) {
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.println("To nie jest liczba");
            }
            int num = sc.nextInt();

            if (num > 0 && num < 50) {
                if (!ArrayUtils.contains(coupon, num)) {
                    coupon[howManyNum - 1] = num;
                    howManyNum--;
                    System.out.println("Podaj kolejną liczbę");
                } else {
                    System.out.println("Ta liczba już została wytypowana");
                }
            } else {
                System.out.println("Podaj liczbę z zakresu 1-49");
            }
        }

        Arrays.sort(coupon);
        System.out.println("Twoje liczby to: " + Arrays.toString(coupon));
        return coupon;
    }

    static int[] lotteryNum(int howManyNum) {
        Random randNum = new Random();
        int[] result = new int[howManyNum];

        while (howManyNum > 0) {

            int num = 1 + randNum.nextInt(48);

            if (!ArrayUtils.contains(result, num)) {
                result[howManyNum - 1] = num;
                howManyNum--;
            }
        }
        Arrays.sort(result);
        System.out.println("Wylosowane liczby to: " + Arrays.toString(result));
        return result;
    }

    static void lottoResult(int[] coupon, int[] lotto) {
        int result = 0;
        for (int i = 0; i < coupon.length; i++) {
            if (ArrayUtils.contains(lotto, coupon[i])) {
                result++;
            }
        }
        
        if (result == lotto.length) {
            System.out.println("GRATULUJĘ GŁÓWNEJ WYGRANEJ");
        } else if (result >= 3) {
            System.out.println("Gratuluję trafienia " + result + "liczb.");
        } else {
            System.out.println("Niestety nic nie wygrałeś");
        }
    }
}
