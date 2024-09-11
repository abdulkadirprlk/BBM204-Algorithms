public class Trie {
    public TrieNode root;

    public Trie() {
        root = new TrieNode('\0');  // root node character is null
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode(k));
        }
        current.isEndOfWord = true;
    }

}
