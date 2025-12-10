package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class GameSession {
    private final List<Player> players;
    private final Scoreboard scoreboard;
    private final WordRepository wordRepository;
    private final Wheel wheel;
    private final Scanner scanner;

    public GameSession(List<Player> players, Scoreboard scoreboard, WordRepository wordRepository, Wheel wheel, Scanner scanner) {
        this.players = players;
        this.scoreboard = scoreboard;
        this.wordRepository = wordRepository;
        this.wheel = wheel;
        this.scanner = scanner;
    }

    public void play() {
        boolean continuePlaying = true;
        while (continuePlaying) {
            RoundState round = new RoundState(wordRepository.randomWord());
            Map<Player, Integer> roundScores = new HashMap<>();
            int turn = 0;
            System.out.println("\nНове слово обране. Починаємо раунд!");
            while (!round.solved()) {
                Player current = players.get(turn % players.size());
                handleTurn(current, round, roundScores);
                turn++;
            }
            concludeRound(round, roundScores);
            continuePlaying = askForNextRound();
        }
    }

    private void handleTurn(Player current, RoundState round, Map<Player, Integer> roundScores) {
        System.out.println("\nСлово: " + round.pattern());
        System.out.println("Букви, що вже були: " + formatGuessed(round.guessedLetters()));
        System.out.printf("Хід гравця %s%n", current.getName());

        int spin = wheel.spin();
        if (spin == Wheel.BANKRUPT) {
            System.out.println("Сектор Банкрут! Раундові очки обнуляються.");
            roundScores.put(current, 0);
            return;
        }
        if (spin == Wheel.SKIP) {
            System.out.println("Пропуск ходу. Наступний гравець!");
            return;
        }

        if (current.isAi()) {
            executeAiTurn(current, round, roundScores, spin);
        } else {
            executeHumanTurn(current, round, roundScores, spin);
        }
    }

    private void executeHumanTurn(Player player, RoundState round, Map<Player, Integer> roundScores, int spin) {
        System.out.printf("Ви вибили %d очок. Введіть букву або спробуйте відгадати слово:%n", spin);
        System.out.print("> ");
        String guess = scanner.nextLine().trim();
        while (guess.isBlank()) {
            System.out.print("Порожній ввід. Спробуйте ще раз: ");
            guess = scanner.nextLine().trim();
        }
        processGuess(player, round, roundScores, spin, guess);
    }

    private void executeAiTurn(Player player, RoundState round, Map<Player, Integer> roundScores, int spin) {
        String guess = AiStrategy.decideGuess(round, wordRepository);
        System.out.printf("ШІ обрав: %s (за %d очок)%n", guess, spin);
        processGuess(player, round, roundScores, spin, guess);
    }

    private void processGuess(Player player, RoundState round, Map<Player, Integer> roundScores, int spin, String guess) {
        String normalized = AiStrategy.normalizeGuess(guess);
        if (normalized.length() == 1) {
            char letter = normalized.charAt(0);
            if (!Character.isLetter(letter)) {
                System.out.println("Потрібна літера. Хід втрачено.");
                return;
            }
            if (round.alreadyGuessed(letter)) {
                System.out.println("Букву вже називали. Хід втрачено.");
                return;
            }
            int occurrences = round.revealLetter(letter);
            if (occurrences > 0) {
                int gained = occurrences * spin;
                roundScores.merge(player, gained, Integer::sum);
                System.out.printf("Є така буква! +%d очок (усього в раунді: %d)%n", gained, roundScores.get(player));
                if (round.solved()) {
                    round.markSolver(player);
                }
            } else {
                System.out.println("Немає такої букви.");
            }
        } else {
            if (round.attemptSolve(normalized, player)) {
                int bonus = Math.max(500, normalized.length() * spin);
                roundScores.merge(player, bonus, Integer::sum);
                System.out.printf("%s відгадав слово і отримує бонус %d очок!%n", player.getName(), bonus);
            } else {
                System.out.println("Невірне слово.");
            }
        }
    }

    private void concludeRound(RoundState round, Map<Player, Integer> roundScores) {
        System.out.println("\nСлово було: " + round.targetWord());
        if (round.solver() != null) {
            System.out.printf("Переможець раунду: %s%n", round.solver().getName());
        }
        for (Map.Entry<Player, Integer> entry : roundScores.entrySet()) {
            scoreboard.recordScore(entry.getKey().getName(), entry.getValue());
        }
        System.out.println("Поточна таблиця лідерів:");
        int place = 1;
        for (ScoreEntry entry : scoreboard.top(5)) {
            System.out.printf("%d. %s — %d%n", place++, entry.name(), entry.score());
        }
    }

    private boolean askForNextRound() {
        System.out.println("Зіграти ще один раунд? (y/n)");
        System.out.print("> ");
        String answer = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        return answer.startsWith("y") || answer.equals("т");
    }

    private String formatGuessed(Iterable<Character> letters) {
        List<Character> list = new ArrayList<>();
        for (Character letter : letters) {
            list.add(letter);
        }
        if (list.isEmpty()) {
            return "поки нічого";
        }
        return list.toString();
    }
}
