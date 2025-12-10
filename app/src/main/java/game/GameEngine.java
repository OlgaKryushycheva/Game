package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameEngine {
    private final Scanner scanner;
    private final Scoreboard scoreboard;
    private final WordRepository wordRepository;
    private final Wheel wheel;

    public GameEngine(Scanner scanner, Scoreboard scoreboard, WordRepository wordRepository, Wheel wheel) {
        this.scanner = scanner;
        this.scoreboard = scoreboard;
        this.wordRepository = wordRepository;
        this.wheel = wheel;
    }

    public void run() {
        boolean running = true;
        System.out.println("Ласкаво просимо до гри \"Поле чудес\"!");
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> startAiGame();
                case "2" -> startMultiplayerGame();
                case "3" -> showScoreboard();
                case "0" -> running = false;
                default -> System.out.println("Невідома команда. Спробуйте ще раз.");
            }
        }
        System.out.println("Дякуємо за гру. До зустрічі!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Оберіть режим:");
        System.out.println("1 - Проти ШІ");
        System.out.println("2 - Декілька гравців");
        System.out.println("3 - Таблиця результатів");
        System.out.println("0 - Вихід");
        System.out.print("> ");
    }

    private void startAiGame() {
        System.out.println("Введіть ваше ім'я:");
        System.out.print("> ");
        String name = readNonEmpty();
        List<Player> players = new ArrayList<>();
        players.add(new Player(name));
        players.add(Player.ai());
        new GameSession(players, scoreboard, wordRepository, wheel, scanner).play();
    }

    private void startMultiplayerGame() {
        System.out.println("Скільки гравців буде грати? (2-4)");
        System.out.print("> ");
        int count = readPlayerCount();
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            System.out.printf("Введіть ім'я гравця %d:%n", i);
            System.out.print("> ");
            players.add(new Player(readNonEmpty()));
        }
        new GameSession(players, scoreboard, wordRepository, wheel, scanner).play();
    }

    private void showScoreboard() {
        System.out.println("\n=== Таблиця результатів ===");
        if (scoreboard.top(10).isEmpty()) {
            System.out.println("Ще немає записів. Зіграй першу партію!");
            return;
        }
        int place = 1;
        for (ScoreEntry entry : scoreboard.top(10)) {
            System.out.printf("%d. %s — %d очок%n", place++, entry.name(), entry.score());
        }
    }

    private String readNonEmpty() {
        String value = scanner.nextLine().trim();
        while (value.isBlank()) {
            System.out.print("Ім'я не може бути порожнім. Спробуйте ще раз: ");
            value = scanner.nextLine().trim();
        }
        return value;
    }

    private int readPlayerCount() {
        while (true) {
            String raw = scanner.nextLine().trim();
            try {
                int result = Integer.parseInt(raw);
                if (result >= 2 && result <= 4) {
                    return result;
                }
            } catch (NumberFormatException ignored) {
                // continue prompting
            }
            System.out.print("Потрібно число від 2 до 4. Повторіть спробу: ");
        }
    }
}
