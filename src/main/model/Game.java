package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.List;

// Game
public class Game implements Writable{
    public static final int TICKS_PER_SECOND = 40;
    private final Player player;
    private final Beast beast;
    private boolean ended;
    private int timer;
    private final int bound;
    private final WordList wordList;
    public int score = 0;
    private final List<Word> words = new ArrayList<>();
    private List<Integer> scores;

    public Game(int bound, WordList wordList) {
        this.bound = bound;
        this.wordList = wordList;
        this.timer = 1200;
        this.player = new Player();
        this.beast = new Beast();
        this.ended = false;
        this.scores = new ArrayList<>();
    }

    //EFFECT: Advance the world state
    public void tick() {
        int t = timer % 40;
        if (t == 0) {
            player.fallBack();
        }
        timer--;

        hasReachedBound();

        if (player.caught() || timer == 0) {
            ended = true;
            EventLog.getInstance().logEvent(new Event("Score added to the score list"));
            return;
        }
    }

    //EFFECT: Keeps the player within the screen
    public void hasReachedBound() {
        if (player.getPlayerPosition().getX() >= bound) {
            player.getPlayerPosition().setX(bound);
        }
    }

    //EFFECT: Advance player away from the beast
    public void advancePlayer() {
        player.advance();
    }

    //EFFECT: checks if the game has ended or not
    public boolean isEnded() {
        return ended;
    }

    //EFFECT: Generate a list of words according to the strings from wordlist
    public void createWords() {
        List<String> stringList = new ArrayList<>(wordList.getWords());
        for (int i = 0; i < 999; i++) {
            createWord(stringList.get(i));
        }
    }

    //EFFECT: Create a word
    private void createWord(String word) {
        words.add(new Word(word));
    }

    public void increaseScore() {
        score++;
    }

    //EFFECT: Reset the game to its original state
    public void reset() {
        this.score = 0;
        this.timer = 1200;
        this.player.reset();
        this.ended = false;
    }

    //EFFECT: Add score to score list
    public void addScore() {
        scores.add(score);
    }

    //EFFECT: Set the game's score list to the parameter score list
    public void setScoreList(List<Integer> scoreList) {
        this.scores = scoreList;
    }


    // EFFECT: Get score
    public int getScore() {
        return score;
    }

    // EFFECT: Get score list
    public List<Integer> getScoreList() {
        return scores;
    }

    // EFFECT: Get time
    public int getTime() {
        return timer / 40;
    }

    //EFFECT: Return an array list of type word
    public List<Word> getWords(){
        return words;
    }

    // EFFECT: Get player
    public Player getPlayer() {
        return player;
    }

    // EFFECT: Get beast
    public Beast getBeast() {
        return beast;
    }

    // JSON RELATED

    //EFFECT: To JSON
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("scores", scoreToJson());
        return json;
    }

    //EFFECT: Put list of scores to JSON
    private JSONArray scoreToJson() {
        JSONArray jsonArray = new JSONArray();

        for (int s : scores) {
            jsonArray.put(scoreJson(s));
        }

        return jsonArray;
    }

    //EFFECT: Put a single score to JSON
    public JSONObject scoreJson(int s) {
        JSONObject json = new JSONObject();
        json.put("int", s);
        return json;
    }

    //EFFECT: For testing purposes
    public void testAddScore(int s) {
        scores.add(s);
    }

    //EFFECT: Print log
    public void printConsole() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString());
        }
    }
}
