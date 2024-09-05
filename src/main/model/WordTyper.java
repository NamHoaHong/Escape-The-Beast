package model;
import java.util.List;

// Handles the user's input
public class WordTyper {
    private Word currentWord;
    public Game game;

    public WordTyper(Game g) {
        currentWord = null;
        game = g;
    }

    // EFFECTS: returns the current word that is being typed
    public Word getCurrentWord() {
        if (currentWord != null) {
            return currentWord;
        }
        return null;
    }

    // EFFECTS: sets the current word to the first word in the wordlist
    public void setCurrentWord(Word currentWord) {
        this.currentWord = currentWord;
    }


    // EFFECTS: Types one character of the current word if c is the next character of the word
    //          If the word has been typed set the currentWord to null
    //          Returns whether progress was made on typing the word
    public boolean typeCurrentWord(char c, List<Word> currentWords) {
        if (currentWord.getNextChar() == c) {
            currentWord.typeName();
            game.increaseScore();
            game.advancePlayer();
            if (currentWord.hasBeenTyped()) {
                currentWords.remove(0);
                currentWord = currentWords.get(0);
            }
            return true;
        }
        return false;
    }


    // EFFECTS: Handles the given input for the player, where:
    //          If there is a current word selected:
    //              type the word according to typeCurrentWord
    //              return whether progress was made on typing the word
    //          If there is no current word:
    //              Set the current word to the first word in the list
    //              Return whether progress was made on typing the word
    public boolean handleInput(List<Word> currentWords, char c) {
        if (currentWord == null) { // no word is selected
            setCurrentWord(currentWords.get(0));
            return typeCurrentWord(c, currentWords);
        } else {
            return typeCurrentWord(c, currentWords);
        }
    }

}
