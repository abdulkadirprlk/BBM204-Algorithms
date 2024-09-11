import java.io.*;
import java.util.*;

public class Quiz4 {
    public static void main(String[] args) throws IOException {
        BufferedReader databaseReader = new BufferedReader(new FileReader(args[0]));
        Map<String, Word> wordMap = new HashMap<>(); // map of Word objects
        Trie trie = new Trie(); // Trie object
        String line;
        long wordCount = Long.parseLong(databaseReader.readLine());
        // creating the database
        while((line = databaseReader.readLine()) != null){
            String[] lineList = line.split("\\s+", 2);
            Word word = new Word(Long.parseLong(lineList[0]), lineList[1].toLowerCase());
            wordMap.put(word.name, word);
            trie.insert(word.name);
        }
        databaseReader.close();

        // applying queries
        BufferedReader queryReader = new BufferedReader(new FileReader(args[1]));
        String line2;
        while((line2 = queryReader.readLine()) != null){
            String[] lineList2 = line2.split("\\s+");
            String prefix = lineList2[0].toLowerCase();
            long limit = Long.parseLong(lineList2[1]);
            List<Word> result = applyQuery(trie, wordMap, prefix, limit);
            printResult(prefix, limit, result);
        }
        queryReader.close();
    }

    private static TrieNode getStartNodeForPrefix(Trie trie, String prefix) {
        TrieNode current = trie.root;
        for (char c : prefix.toCharArray()) {
            TrieNode node = current.children.get(c);
            if (node == null) {
                return null;  // prefix not found
            }
            current = node;
        }
        return current;
    }

    private static void findAllWords(TrieNode node, String prefix, PriorityQueue<Word> pq, Map<String, Word> wordMap) {
        if (node.isEndOfWord) {
            Word word = wordMap.get(prefix);
            if (word != null) {
                pq.add(word);
            }
        }
        for (char c : node.children.keySet()) {
            findAllWords(node.children.get(c), prefix + c, pq, wordMap);
        }
    }

    private static List<Word> applyQuery(Trie trie, Map<String, Word> wordMap, String prefix, long limit){
        List<Word> result = new ArrayList<>();
        PriorityQueue<Word> pq = new PriorityQueue<>((a, b) -> {
            int weightComparison = Long.compare(b.weight, a.weight);
            if (weightComparison != 0) {
                return weightComparison;
            } else {
                return a.name.compareTo(b.name);
            }
        });
        TrieNode startNode = getStartNodeForPrefix(trie, prefix);
        if (startNode != null) {
            findAllWords(startNode, prefix, pq, wordMap);
            while (!pq.isEmpty() && limit-- > 0) {
                result.add(pq.poll());
            }
        }
        return result;
    }

    private static void printResult(String prefix, long limit, List<Word> listOfWords){
        System.out.println("Query received: \"" + prefix + "\" with limit " + limit + ". Showing results:");
        if (listOfWords.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        for (Word word : listOfWords) {
            System.out.println("- " + word.weight + " " + word.name);
        }
    }
}
