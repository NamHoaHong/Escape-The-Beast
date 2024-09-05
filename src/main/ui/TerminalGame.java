package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.Scrollable;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.*;
import org.w3c.dom.Text;
import persistance.JsonReader;
import persistance.JsonWriter;

// Game Terminal
public class TerminalGame {
    private Game game;
    private Screen screen;
    private WordTyper wordTyper;
    private static final String JSON_STORE = "./data/game.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private WindowBasedTextGUI endGui;
    private WordList wl;

    // Creates JSON writer and reader
    public TerminalGame() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS:  initializes the terminal and starts menu from a given wordlist
    public void start(WordList wordList) throws IOException, InterruptedException {
        try {
            //Create Screen
            screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            screen.setCursorPosition(null);
            TextGraphics textGraphicsMenu = screen.newTextGraphics();
            textGraphicsMenu.setForegroundColor(TextColor.ANSI.GREEN);
            TerminalSize terminalSize = screen.getTerminalSize();
            textGraphicsMenu.putString(32, 2, "Escape The Beast");
            textGraphicsMenu.putString(35, 8, "s -> Start");
            textGraphicsMenu.putString(30, 10, "v -> View Scores");
            textGraphicsMenu.putString(25, 12, "l -> Load Scores From Previous Saves");
            textGraphicsMenu.putString(35, 14, "q -> Quit");
            screen.refresh();

            //Save word list
            this.wl = wordList;

            //Create game
            game = new Game(
                    terminalSize.getColumns(),
                    wordList
            );
            wordTyper = new WordTyper(game);
            game.createWords();

            // Prompts user to choose an option
            KeyStroke keyStroke = screen.readInput();
            while (keyStroke != null) {
                if (keyStroke.getCharacter() == 'q') {
                    game.printConsole();
                    System.exit(0);
                } else if (keyStroke.getCharacter() == 's') {
                    screen.clear();
                    renderWaitRoom();
                    beginTicks(wordList);
                } else if (keyStroke.getCharacter() == 'v') {
                    viewScores(game);
                } else if (keyStroke.getCharacter() == 'l') {
                    loadGames(game);
                } keyStroke = screen.readInput();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: Begins ticking until the game is over, then prompts the user to save
    private void beginTicks(WordList wordList) throws IOException, InterruptedException {
        while (!game.isEnded()) {
            tick();
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);
        }
    }

    // EFFECT: Begins to tick
    private void tick() throws IOException {
        handleUserInput();

        game.tick();

        screen.clear();
        render();
        screen.refresh();
    }

    // EFFECTS: Checks for user input, if one exists, calls wordTyper to handle input
    private void handleUserInput() throws IOException {
        KeyStroke keyStroke = screen.pollInput();

        if (keyStroke == null || keyStroke.getCharacter() == null) {
            return;
        }
        char c = keyStroke.getCharacter();
        wordTyper.handleInput(game.getWords(), c);
    }

    //EFFECT: Saves game score
    private void saveGame(Game game) {
        try {
            game.addScore();
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //EFFECT: View saved scores and lets user sorts it by highest score, lowest score and oldest
    private void viewScores(Game game) {
        try {
            List<Integer> scoreList = game.getScoreList();
            List<Integer> sortedList = new ArrayList<>();
            for (int s: scoreList) {
                sortedList.add(s);
            }
            screen.clear();
            screen.scrollLines(1, scoreList.size(), 2);
            TerminalSize terminalSize = screen.getTerminalSize();
            TextGraphics textGraphics = screen.newTextGraphics();
            int i = 1;
            for (int s: scoreList) {
                String l = String.valueOf(s);
                textGraphics.putString(terminalSize.getColumns() / 3, i, l);
                i++;
            }
            textGraphics.putString(2 * terminalSize.getColumns() / 3, 8, "d -> Date");
            textGraphics.putString(2 * terminalSize.getColumns() / 3, 10, "h -> Highest");
            textGraphics.putString(2 * terminalSize.getColumns() / 3, 12, "l -> Lowest");
            textGraphics.putString(2 * terminalSize.getColumns() / 3, 14, "b -> Back");
            screen.refresh();

            KeyStroke keyStroke = screen.readInput();
            while (keyStroke != null) {
                if (keyStroke.getCharacter() == 'd') {
                    screen.clear();
                    int x = 1;
                    for (int s: scoreList) {
                        String l = String.valueOf(s);
                        textGraphics.putString(terminalSize.getColumns() / 3, x, l);
                        x++;
                    }
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 8, "d -> Date");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 10, "h -> Highest");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 12, "l -> Lowest");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 14, "b -> Back");
                    screen.refresh();
                } else if (keyStroke.getCharacter() == 'h') {
                    Collections.sort(sortedList, Collections.reverseOrder());
                    screen.clear();
                    int x = 1;
                    for (int s: sortedList) {
                        String l = String.valueOf(s);
                        textGraphics.putString(terminalSize.getColumns() / 3, x, l);
                        x++;
                    }
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 8, "d -> Date");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 10, "h -> Highest");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 12, "l -> Lowest");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 14, "b -> Back");
                    screen.refresh();
                } else if (keyStroke.getCharacter() == 'l') {
                    Collections.sort(sortedList);
                    screen.clear();
                    int x = 1;
                    for (int s: sortedList) {
                        String l = String.valueOf(s);
                        textGraphics.putString(terminalSize.getColumns() / 3, x, l);
                        x++;
                    }
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 8, "d -> Date");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 10, "h -> Highest");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 12, "l -> Lowest");
                    textGraphics.putString(2 * terminalSize.getColumns() / 3, 14, "b -> Back");
                    screen.refresh();
                } else if (keyStroke.getCharacter() == 'b') {
                    screen.close();
                    start(wl);
                } keyStroke = screen.readInput();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EFFECT: Load the save data from JSON file
    private void loadGames(Game game) {
        try {
            List<Integer> scoreList = jsonReader.read();
            game.setScoreList(scoreList);
            System.out.println("Loaded scores from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // RENDERING

    //EFFECT: Render the wait room before getting in the game
    private void renderWaitRoom() {
        try {
            TerminalSize terminalSize = screen.getTerminalSize();
            TextGraphics heart = screen.newTextGraphics();
            heart.setForegroundColor(TextColor.ANSI.RED);
            for (int i = 1; i<=10; i += 0) {
                if (i % 2 == 1) {
                    String countdown = String.valueOf((10 - i + 1) / 2);
                    heart.putString(terminalSize.getColumns() / 2, 2, countdown);
                    heart.putString(30, 5, "  *******   *******  ");
                    heart.putString(30, 6, " ********* ********* ");
                    heart.putString(30, 7, "*********************");
                    heart.putString(30, 8, "*********************");
                    heart.putString(30, 9, " ******************* ");
                    heart.putString(30, 10, "  *****************  ");
                    heart.putString(30, 11, "   ***************   ");
                    heart.putString(30, 12, "    *************    ");
                    heart.putString(30, 13, "     ***********     ");
                    heart.putString(30, 14, "      *********      ");
                    heart.putString(30, 15, "       *******     ");
                    heart.putString(30, 16, "        *****      ");
                    heart.putString(30, 17, "         ***       ");
                    screen.refresh();
                    Thread.sleep(500);
                    screen.clear();
                    i++;
                } else {
                    heart.putString(28, 4, "  *********   *********  ");
                    heart.putString(28, 5, " *********** *********** ");
                    heart.putString(28, 6, "*************************");
                    heart.putString(28, 7, "*************************");
                    heart.putString(28, 8, " *********************** ");
                    heart.putString(28, 9, "  *********************  ");
                    heart.putString(28, 10, "   *******************   ");
                    heart.putString(28, 11, "    *****************    ");
                    heart.putString(28, 12, "     ***************     ");
                    heart.putString(28, 13, "      *************      ");
                    heart.putString(28, 14, "       ***********     ");
                    heart.putString(28, 15, "        *********      ");
                    heart.putString(28, 16, "         *******       ");
                    heart.putString(28, 17, "          *****       ");
                    heart.putString(28, 18, "           ***       ");
                    screen.refresh();
                    Thread.sleep(500);
                    screen.clear();
                    i++;
                }
            }
            screen.clear();
            screen.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EFFECT: Render the game
    private void render() {
        if (game.isEnded()) {
            if (endGui == null) {
                drawEndScreen();
            }

            return;
        }

        drawBeast();
        drawPlayer();
        drawScore();
        drawTime();
        drawWord();
        drawTypedPortion();
    }

    //EFFECT: Draw the countdown timer
    private void drawTime() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        String s = String.valueOf(game.getTime());
        text.putString(70, 1, s);
    }

    //EFFECT: Draw the current word that needs to be typed
    private void drawWord() {
        TerminalSize terminalsize = screen.getTerminalSize();
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(terminalsize.getColumns() / 2, 5, game.getWords().get(0).getString());
    }

    //EFFECT: Draw the typed portion of the current word
    private void drawTypedPortion() {
        TerminalSize terminalsize = screen.getTerminalSize();
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.BLUE);
        text.putString(terminalsize.getColumns() / 2, 5, game.getWords().get(0).getTypedPortion());
    }

    //EFFECT: Draw the beast
    private void drawBeast() {
        Beast beast = game.getBeast();
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(beast.getBeastPosition().getX(), 10, "  /\\\\_/\\\\");
        text.putString(beast.getBeastPosition().getX(), 11, " / o o \\\\");
        text.putString(beast.getBeastPosition().getX(), 12, "(   \"   )");
        text.putString(beast.getBeastPosition().getX(), 13, " \\\\___/");
    }

    //EFFECT: Draw the player
    private void drawPlayer() {
        Player player = game.getPlayer();
        drawPosition(player.getPlayerPosition(), TextColor.ANSI.GREEN, '\u2588', true);
    }

    //EFFECT: Draw a rectangle that represents the player
    private void drawPosition(Position pos, TextColor color, char c, boolean wide) {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(color);
        text.putString(pos.getX(), 12, String.valueOf(c));

        if (wide) {
            text.putString(pos.getX() + 1, 12, String.valueOf(c));
        }
    }

    //EFFECT: Draw the score
    private void drawScore() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(1, 0, "Score: ");

        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(8, 0, String.valueOf(game.getScore()));
    }

    //EFFECT: Draw the end screen which display the score of the user and prompt the user whether to save the score
    private void drawEndScreen() {
        endGui = new MultiWindowTextGUI(screen);

        Window window = new BasicWindow("Game Over!");
        Panel contentPanel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);
        Label message = new Label("You finished with a score of " + game.getScore() + "!");
        message.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1));
        contentPanel.addComponent(message);
        Label message2 = new Label("Do you want to save your score ?");
        message2.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1));
        contentPanel.addComponent(message2);
        contentPanel.addComponent(new Button("Yes", () -> {
            try {
                saveGame(game);
                game.reset();
                screen.close();
                endGui = null;
                wl.shuffle();
                start(wl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        contentPanel.addComponent(new Button("No", () -> {
            try {
                game.reset();
                screen.close();
                endGui = null;
                wl.shuffle();
                start(wl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        window.setComponent(contentPanel);
        endGui.addWindowAndWait(window);
    }
}
