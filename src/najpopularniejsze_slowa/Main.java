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
import java.util.ArrayList;

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
        int minWordLenght = 3;
        String[] censor = {"przez", "oraz", "jak", "kto", "się", "czyli", "dla"};


        savePopularWords(scanWebForTitlesAndSplitWords(sitesAndSelectors), popWords);
        readAndSaveFilteredWords(popWords, filteredPopWords, minWordLenght, censor);


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
            Files.write(destPath, filteredList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
