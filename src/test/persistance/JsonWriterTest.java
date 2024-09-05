package persistance;

import model.Game;
import model.WordList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterNormalGame() {
        try {
            WordList wordList = new WordList(Path.of("src/Dictionary.txt"));
            Game game = new Game(50, wordList);
            game.testAddScore(5);
            game.testAddScore(7);
            JsonWriter writer = new JsonWriter("./data/testWriterNormalGame.json");
            writer.open();
            writer.write(game);
            writer.close();

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
