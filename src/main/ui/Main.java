package ui;

import model.WordList;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) throws Exception {
        WordList wordList = new WordList(Path.of("src/Dictionary.txt"));
        TerminalGame game = new TerminalGame();
        game.start(wordList);
    }
}
