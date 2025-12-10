package game;

import java.nio.file.Path;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Scoreboard scoreboard = new Scoreboard(Path.of("scoreboard.txt"));
            GameEngine engine = new GameEngine(scanner, scoreboard, new WordRepository(), new Wheel());
            engine.run();
        }
    }
}
