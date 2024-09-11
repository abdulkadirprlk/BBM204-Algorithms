import java.util.HashMap;

public class TrieNode {
    char character;
    HashMap<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;

    public TrieNode (char character) {
        this.character = character;
    }
}
