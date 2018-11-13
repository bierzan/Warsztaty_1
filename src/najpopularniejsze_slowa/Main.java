package najpopularniejsze_slowa;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    final static String DATE = String.valueOf(java.time.LocalDate.now());
    final static String[][] SITES_AND_SELECTORS = {
            {"http://www.onet.pl", "span.title"},
            {"http://www.gazeta.pl", ".o-article__header"},
            {"http://www.rzeczpospolita.pl", ".teaser__title"},
            {"http://www.tvn24.pl", "article"},
            {"http://www.gpcodziennie.pl", ".title"}
    };

    public static void main(String[] args) {


        String popWords = "popular_words.txt";
        String words = "src/popular_words/pop_words_" + DATE + ".txt";
        int minWordLength = 3;
        String[] censor = {"ale", "przez", "oraz", "jak", "kto", "się", "czyli", "dla", "nie", "jest", "już", "będzie", "może", "czy"};

        savePopularWords(scanWebForTitlesAndSplitWords(SITES_AND_SELECTORS), popWords);

        String wordsList = wordsByOccur(countAndSortWords(readAndFilter(popWords, minWordLength, censor)));

        saveWithDate(wordsList, words);

    }

    // czytanie wyrazow z artykulow i zapis do jako string
    private static String[] scanWebForTitlesAndSplitWords(String[][] webAndSelector) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < webAndSelector.length; i++) {
            Connection connect = Jsoup.connect(webAndSelector[i][0]);
            try {
                Document document = connect.get();
                Elements links = document.select(webAndSelector[i][1]);
                for (Element elem : links) {
                    sb.append(elem.text() + " ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().split("[,.\\-:; \'\"]");
    }

    // zapis znalezionych wyrazow do pliku
    private static void savePopularWords(String[] str, String fileName) {
        Path path = Paths.get(fileName);
        ArrayList<String> wordsList = new ArrayList<>();

        for (String word : str) {
            wordsList.add(StringUtils.lowerCase(word));
        }
        try {
            Files.write(path, wordsList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // filtruje za krotkie wyrazy i te z listy zakazanych
    private static String[] readAndFilter(String resFile, int minWordLenght, String[] wordsToRemove) {
        Path resPath = Paths.get(resFile);

        ArrayList<String> filteredList = new ArrayList<>();

        try {
            for (String line : Files.readAllLines(resPath)) {
                if (line.length() >= minWordLenght && !ArrayUtils.contains(wordsToRemove, line)) {
                    filteredList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arrToTab = new String[filteredList.size()];
        return arrToTab = filteredList.toArray(arrToTab);
    }

    // liczy ilosc wystapien każdego wyrazu
    private static String[][] countAndSortWords(String[] words) {

        String[] wordsList = Arrays.copyOf(words, words.length);
        Set<String> wordsSet = new HashSet<String>(Arrays.asList(words));

        String[] uniqueWords = wordsSet.toArray(new String[0]);
        int[] countWords = new int[uniqueWords.length];

        for (int i = 0; i < uniqueWords.length; i++) {
            int count = 0;
            for (int j = 0; j < wordsList.length; j++) {
                if (uniqueWords[i].equals(wordsList[j])) {
                    count++;
                    countWords[i] = count;
                }
            }
        }

        String[][] countedWords = new String[uniqueWords.length][2];

        for (int i = 0; i < countedWords.length; i++) {
            countedWords[i][0] = String.valueOf(countWords[i]);
            countedWords[i][1] = uniqueWords[i];
        }

        //sortowanie wyrazow 2d tabeli wedlug pierwszej pozycji
        Arrays.sort(countedWords, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                String itemOne = o1[0];
                String itemTwo = o2[0];
                return Integer.valueOf(itemTwo).compareTo(Integer.valueOf(itemOne));
            }
        });

        return countedWords;
    }

    // grupuje słowa według liczby wystąpień
    private static String wordsByOccur(String[][] numAndWord) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        ArrayList<String> a = new ArrayList<>();

        for (int i = 0; i < numAndWord.length - 1; i++) {

            if (a.size() == 0) {
                a.add(numAndWord[i][0]);
            }
            a.add(numAndWord[i][1]);

            if (!a.get(0).equals(numAndWord[i + 1][0])) {
                result.add(new ArrayList<>(a));
                a.clear();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DATE);
        sb.append("\n");
        sb.append("ŹRÓDŁA:\n");
        for (String[] line : SITES_AND_SELECTORS) {
            sb.append(line[0]);
            sb.append("\n");
        }

        for (int i = 0; i < result.size(); i++) {
            sb.append("\nSŁOWA POWTÓRZONE " + result.get(i).get(0) + "RAZY: \n");
            int count = 0;

            for (int j = 1; j < result.get(i).size(); j++) {
                sb.append("| " + result.get(i).get(j));
                int white = 21 - result.get(i).get(j).length();

                while (white > 0) {
                    sb.append(" ");
                    white--;
                }

                if (count < 3) {
                    count++;
                } else {
                    sb.append("\n");
                    count = 0;
                }
            }
            sb.append("\n-------------------------------\n");
        }
        String report = sb.toString();
        return report;
    }

    //zapis do pliku
    private static void saveWithDate(String str, String destFile) {

        Path destPath = Paths.get(destFile);

        ArrayList<String> finalList = new ArrayList<>();
        finalList.add(str);
        try {
            Files.write(destPath, finalList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
