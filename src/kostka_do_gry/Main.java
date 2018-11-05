package kostka_do_gry;


import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        dieRoll();

    }

    private static int dieRoll() {
        String rollType = dieRollType();

        int howManyRolls = rollsNum(rollType);
        int dieSize = howManySides(rollType);
        int modificator = modValue(rollType);
        int result = 0;

        Random rand = new Random();

        for (int i = 1; i <= howManyRolls; i++) {
            int num = rand.nextInt(dieSize) + 1;
            result += num;
            System.out.println(i + "rzut - wyrzucono: " + num);
        }

        System.out.println("Dodano modyfikator " + modificator);
        result += modificator;
        System.out.println("Wynik rzutu wynosi " + result);
        return result;

    }

    private static String dieRollType() {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        String diceNum = null;
        String diceSides = null;
        String rollModificator = null;

        System.out.println("Podaj liczbę rzutów");
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.println("Podana wartość nie jest liczbą");
        }
        diceNum = String.valueOf(sc.nextInt());

        if (Integer.valueOf(diceNum) > 1) {
            sb.append(diceNum);
        }

        System.out.println("Podaj liczbę ścian kostki");
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.println("Podana wartość nie jest liczbą");
        }
        diceSides = String.valueOf(sc.nextInt());
        sb.append("D" + diceSides);

        System.out.println("Podaj modyfikator");
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.println("Podana wartość nie jest liczbą");
        }
        rollModificator = String.valueOf(sc.nextInt());
        if (Integer.valueOf(rollModificator) > 0) {

            sb.append("+" + rollModificator);
        } else if (Integer.valueOf(rollModificator) < 0) {
            sb.append(rollModificator);
        }

        return sb.toString();
    } //metoda do generowania zapisu rzutu kostką

    private static int rollsNum(String rollType) {
        int result = 1;
        String[] rollCodeSplit = rollType.split("D");

        if (!rollCodeSplit.equals("D")) {
            result = Integer.valueOf(rollCodeSplit[0]);
        }
        return result;
    }

    private static int howManySides(String rollType) {
        int result;
        String[] rollCodeSplit = rollType.split("D");

        if (rollCodeSplit[1].contains("+")) {
            String[] rollCodeSplit2 = rollCodeSplit[1].split("+");
            result = Integer.valueOf(rollCodeSplit2[0]);
        } else if (rollCodeSplit[1].contains("-")) {
            String[] rollCodeSplit2 = rollCodeSplit[1].split("-");
            result = Integer.valueOf(rollCodeSplit2[0]);
        } else {
            result = Integer.valueOf(rollCodeSplit[rollCodeSplit.length - 1]);
        }
        return result;
    }

    private static int modValue(String rollType) {
        int result = 0;
        if (rollType.contains("+")) {
            String[] rollSplit = rollType.split("+");
            result = Integer.valueOf(rollSplit[rollSplit.length - 1]);
        } else if (rollType.contains("-")) {
            String[] rollSplit = rollType.split("-");
            result = Integer.valueOf(rollSplit[rollSplit.length - 1]) * -1;
        }
        return result;
    }
}
