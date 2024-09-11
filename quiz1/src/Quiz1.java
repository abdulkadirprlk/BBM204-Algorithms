import java.util.*;
import java.io.*;

public class Quiz1 {
    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.ENGLISH);
        // TODO: Use the first command line argument (args[0]) as the file to read the input from

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        List<String> wordsToIgnore = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> keywords = new ArrayList<>();

        boolean foundDelimiter = false;
        String line;
        while((line = reader.readLine()) != null){
            if(line.equals("...")){
                foundDelimiter = true;
                continue;
            }
            if (!foundDelimiter) {
                line = line.toLowerCase();
                wordsToIgnore.add(line);
            } else {
                line = line.toLowerCase();
                titles.add(line);
            }
        }
        reader.close();

        // TODO: Your code goes here
        for(String s : titles){
            String[] keywordList = s.split(" ");
            for(String word : keywordList){
                word = word.toLowerCase();
                if(!wordsToIgnore.contains(word) && !keywords.contains(word)){
                    keywords.add(word);
                }
            }
        }
        Collections.sort(keywords);

        // TODO: Print the solution to STDOUT
        generateKWICIndex(titles, wordsToIgnore, keywords);
    }
    public static void generateKWICIndex(List<String> titles, List<String> wordsToIgnore, List<String> keywords) {
        TreeMap<String, List<String>> keywordLinesMap = new TreeMap<>();

        for (String title : titles) {
            String[] titleWordsList = title.split(" ");
            for (String word : titleWordsList) {
                if (keywords.contains(word.toLowerCase()) && !wordsToIgnore.contains(word.toLowerCase())) {
                    StringBuilder lineBuilder = new StringBuilder();
                    for (String titleWord : titleWordsList) {
                        if (titleWord.equalsIgnoreCase(word)) {
                            lineBuilder.append(titleWord.toUpperCase()).append(" ");
                        } else {
                            lineBuilder.append(titleWord).append(" ");
                        }
                    }
                    String line = lineBuilder.toString().trim();
                    if (!keywordLinesMap.containsKey(word.toLowerCase())) {
                        keywordLinesMap.put(word.toLowerCase(), new ArrayList<>());
                    }
                    keywordLinesMap.get(word.toLowerCase()).add(line);
                }
            }
        }
        for (Map.Entry<String, List<String>> entry : keywordLinesMap.entrySet()) {
            for (String line : entry.getValue()) {
                System.out.println(line);
            }
        }
    }
}
