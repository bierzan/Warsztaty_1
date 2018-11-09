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
    public static void main(String[] args) {

        String[][] sitesAndSelectors = {
                {"http://www.onet.pl", "span.title"},
                {"http://www.gazeta.pl", ".o-article__header"},
                {"http://www.rzeczpospolita.pl", ".teaser__title"},
                {"http://www.tvn24.pl", "article"},
                {"http://www.gpcodziennie.pl", ".title"}
        };

        String popWords = "popular_words.txt";
        String filteredPopWords = "filtered_popular_words";
        int minWordLength = 3;
        String[] censor = {"przez", "oraz", "jak", "kto", "się", "czyli", "dla", "nie", "jest", "już", "będzie", "może", "czy"};


        savePopularWords(scanWebForTitlesAndSplitWords(sitesAndSelectors), popWords);
        readAndSaveFilteredWords(popWords, filteredPopWords, minWordLength, censor);
        

    }

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

    private static void readAndSaveFilteredWords(String resFile, String destFile, int minWordLenght, String[] wordsToRemove) {
        Path resPath = Paths.get(resFile);
        Path destPath = Paths.get(destFile);

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
        try {
            Files.write(destPath, filteredList); //potem zapisywanie zostnaie przeniesiony po pogrupowaniu plikow
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arrToTab = new String[filteredList.size()];
        arrToTab = filteredList.toArray(arrToTab);

        String[][] str= groupWordsByOccur(countAndSortWords(arrToTab));
        wordsByOccur(str);

    }

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

        return countedWords;
    }

    private static String[][] groupWordsByOccur(String[][] countedWords) {

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

    private static ArrayList<ArrayList<String>> wordsByOccur(String[][] numAndWord){
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        ArrayList<String> a = new ArrayList<>();

        for(int i = 0; i<numAndWord.length-1; i++){

            if(a.size()==0){
                a.add(numAndWord[i][0]);
            }
            a.add(numAndWord[i][1]);

            if(!a.get(0).equals(numAndWord[i+1][0])){
                System.out.println(ArrayUtils.toString(a));
                result.add(a);
                a.clear();
            }

        }
        return result;

    }


}
