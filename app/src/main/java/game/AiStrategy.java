package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AiStrategy {
    private static final List<Character> LETTER_PRIORITY = List.of(
            'О', 'А', 'И', 'Н', 'Е', 'Т', 'С', 'Р', 'І', 'В', 'Л', 'К', 'М', 'Д', 'П', 'У', 'Я', 'З', 'Б', 'Г'
    );

    private AiStrategy() {
    }

    public static String decideGuess(RoundState state, WordRepository repository) {
        if (state.missingLetters() <= 3) {
            String proposal = repository.guessByPattern(state.pattern().replace(" ", ""), new ArrayList<>(state.guessedLetters()));
            if (proposal != null) {
                return proposal;
            }
        }
        return String.valueOf(pickLetter(state.guessedLetters()));
    }

    private static char pickLetter(Set<Character> guessed) {
        for (char candidate : LETTER_PRIORITY) {
            if (!guessed.contains(candidate)) {
                return candidate;
            }
        }
        return guessed.stream().min(Comparator.naturalOrder()).orElse('А');
    }

    public static String normalizeGuess(String guess) {
        return guess.toUpperCase(Locale.forLanguageTag("uk"));
    }
}
