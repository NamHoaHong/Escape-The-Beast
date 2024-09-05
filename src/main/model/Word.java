package model;

// A word type which contains a word string and the typed progress of the word
public class Word {
    private int nextToTypeIndex;
    private final String word;

    public Word(String word) {
        this.word = word;
        this.nextToTypeIndex = 0;
    }

    // EFFECTS: returns the next character to be typed by the player for this word
    public char getNextChar() {
        return word.charAt(nextToTypeIndex);
    }

    // EFFECTS: returns whether the word has been typed
    public boolean hasBeenTyped() {
        return nextToTypeIndex >= word.length();
    }

    // EFFECTS: 'types' the next character in the word
    public void typeName() {
        nextToTypeIndex++;
    }

    // EFFECTS: resets this word's progress to 0
    public void resetProgress() {
        nextToTypeIndex = 0;
    }

    public String getString() {
        return this.word;
    }

    public String getTypedPortion() {
        return this.word.substring(0, nextToTypeIndex);
    }

    public String getUntypedPortion() {
        return this.word.substring(nextToTypeIndex);
    }

    public int getNextToTypeIndex() {
        return nextToTypeIndex;
    }
}
