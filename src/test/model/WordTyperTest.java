package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordTyperTest {
    private List<Word> words;
    private Word word1;
    private Word word2;
    private WordTyper wordTyper;
    private WordList wordList;
    private Game game;


    @BeforeEach
    public void setUp(){
        try {
            word1 = new Word("Hello");
            word2 = new Word("World");
            words = new ArrayList<Word>();
            wordList = new WordList(Path.of("src/Dictionary.txt"));
            game = new Game(5, wordList);
            words.add(word1);
            words.add(word2);
            wordTyper = new WordTyper(game);
        } catch (IOException e) {
            fail("Should not fail");
        }
    }

    @Test
    public void getAndSetCurrentWordTest(){
        // No word is selected
        assertEquals(wordTyper.getCurrentWord(), null);

        // Set the word to word1
        wordTyper.setCurrentWord(word1);
        assertEquals(wordTyper.getCurrentWord(), word1);

        // Set the word to word2
        wordTyper.setCurrentWord(word2);
        assertEquals(wordTyper.getCurrentWord(), word2);
    }

    @Test
    public void typeCurrentWordTest(){
        wordTyper.setCurrentWord(word1);
        assertTrue(wordTyper.typeCurrentWord('H', words));
        assertEquals(game.getScore(), 1);
        assertFalse(wordTyper.typeCurrentWord('a', words));
        assertTrue(wordTyper.typeCurrentWord('e', words));
        assertEquals(game.getScore(), 2);
        assertFalse(wordTyper.typeCurrentWord('o', words));
        assertTrue(wordTyper.typeCurrentWord('l', words));
        assertEquals(game.getScore(), 3);
        assertTrue(wordTyper.typeCurrentWord('l', words));
        assertEquals(game.getScore(), 4);
        assertTrue(wordTyper.typeCurrentWord('o', words));
        assertEquals(game.getScore(), 5);
        assertEquals(words.size(), 1);
        assertEquals(wordTyper.getCurrentWord(), null);
    }

    @Test
    public void handleInputTest(){
        // No words typed
        assertEquals(wordTyper.getCurrentWord(), null);

        // Starts typing
        assertFalse(wordTyper.handleInput(words, 'a'));

        // First word of the list
        assertTrue(wordTyper.handleInput(words, 'H'));
        assertEquals(game.getScore(), 1);
        assertFalse(wordTyper.handleInput(words, 'a'));
        assertTrue(wordTyper.handleInput(words, 'e'));
        assertEquals(game.getScore(), 2);
        assertFalse(wordTyper.handleInput(words, 'o'));
        assertTrue(wordTyper.handleInput(words, 'l'));
        assertEquals(game.getScore(), 3);
        assertTrue(wordTyper.handleInput(words, 'l'));
        assertEquals(game.getScore(), 4);
        assertTrue(wordTyper.handleInput(words, 'o'));
        assertEquals(game.getScore(), 5);
        assertEquals(words.size(), 1);

        // Second word of the list
        assertFalse(wordTyper.handleInput(words, 'o'));
        assertEquals(wordTyper.getCurrentWord(), word2);
        assertTrue(wordTyper.handleInput(words, 'W'));
        assertEquals(game.getScore(), 6);
        assertFalse(wordTyper.handleInput(words, 'a'));
        assertTrue(wordTyper.handleInput(words, 'o'));
        assertEquals(game.getScore(), 7);
        assertFalse(wordTyper.handleInput(words, 'o'));
        assertTrue(wordTyper.handleInput(words, 'r'));
        assertEquals(game.getScore(), 8);
        assertTrue(wordTyper.handleInput(words, 'l'));
        assertEquals(game.getScore(), 9);
        assertTrue(wordTyper.handleInput(words, 'd'));
        assertEquals(game.getScore(), 10);
    }
}
