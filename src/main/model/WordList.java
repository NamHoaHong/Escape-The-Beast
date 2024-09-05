package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// A list of words from the path
public class WordList {
    private List<String> words;
    private Random random = new Random();

    // EFFECTS: Reads file at path
    public WordList(Path wordFilePath) throws IOException {
        this.words = Files.readAllLines(wordFilePath);
        Collections.shuffle(words);
    }

    // EFFECTS : Returns the wordlist in the List<String> format
    public List<String> getWords() {
        return words;
    }

    // EFFECTS : Shuffle the wordlist
    public void shuffle() {
        Collections.shuffle(words);
    }
}
