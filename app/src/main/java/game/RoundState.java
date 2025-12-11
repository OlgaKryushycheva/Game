package game;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class RoundState {
    private final String targetWord;
    private final String hint;
    private final char[] progress;
    private final Set<Character> guessedLetters = new HashSet<>();
    private Player solver;

    public RoundState(String targetWord) {
        this(targetWord, null);
    }

    public RoundState(WordEntry entry) {
        this(entry.word(), entry.hint());
    }

    private RoundState(String targetWord, String hint) {
        this.targetWord = targetWord.toUpperCase(Locale.forLanguageTag("uk"));
        this.hint = hint;
        this.progress = new char[targetWord.length()];
        for (int i = 0; i < progress.length; i++) {
            char c = this.targetWord.charAt(i);
            progress[i] = Character.isLetter(c) ? '_' : c;
        }
    }

    public boolean solved() {
        for (char c : progress) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    public Player solver() {
        return solver;
    }

    public void markSolver(Player solver) {
        this.solver = solver;
    }

    public int revealLetter(char letter) {
        char normalized = Character.toUpperCase(letter);
        if (guessedLetters.contains(normalized)) {
            return 0;
        }
        guessedLetters.add(normalized);
        int revealed = 0;
        for (int i = 0; i < targetWord.length(); i++) {
            if (targetWord.charAt(i) == normalized) {
                progress[i] = normalized;
                revealed++;
            }
        }
        return revealed;
    }

    public boolean attemptSolve(String guess, Player player) {
        String normalized = guess.toUpperCase(Locale.forLanguageTag("uk"));
        if (!normalized.equals(targetWord)) {
            return false;
        }
        for (int i = 0; i < targetWord.length(); i++) {
            progress[i] = targetWord.charAt(i);
        }
        solver = player;
        return true;
    }

    public boolean alreadyGuessed(char letter) {
        return guessedLetters.contains(Character.toUpperCase(letter));
    }

    public String pattern() {
        StringBuilder builder = new StringBuilder();
        for (char c : progress) {
            builder.append(c).append(' ');
        }
        return builder.toString().trim();
    }

    public int missingLetters() {
        int count = 0;
        for (char c : progress) {
            if (c == '_') {
                count++;
            }
        }
        return count;
    }

    public Set<Character> guessedLetters() {
        return Set.copyOf(guessedLetters);
    }

    public String targetWord() {
        return targetWord;
    }

    public String hint() {
        return hint;
    }
}
