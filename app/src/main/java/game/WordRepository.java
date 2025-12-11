package game;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class WordRepository {
    private final List<String> words = Arrays.asList(
            "КОМП'ЮТЕР",
            "ІНТЕЛЕКТ",
            "ПРОГРАМА",
            "АЛГОРИТМ",
            "БІБЛІОТЕКА",
            "СТРУКТУРА",
            "КОНСТРУКТОР",
            "СЕРІАЛІЗАЦІЯ",
            "ПАРАЛЕЛЬНІСТЬ",
            "ІНКАПСУЛЯЦІЯ",
            "ГАЛАКТИКА",
            "КОСМІЧНИЙ",
            "ПРИГОДА",
            "ДЖАВА",
            "СИМУЛЯЦІЯ"
    );

    public String randomWord() {
        int index = ThreadLocalRandom.current().nextInt(words.size());
        return words.get(index).toUpperCase(Locale.forLanguageTag("uk"));
    }

    public String guessByPattern(String pattern, List<Character> excluded) {
        for (String word : words) {
            if (matches(pattern, word, excluded)) {
                return word;
            }
        }
        return null;
    }

    private boolean matches(String pattern, String word, List<Character> excluded) {
        if (pattern.length() != word.length()) {
            return false;
        }
        for (int i = 0; i < pattern.length(); i++) {
            char patternChar = pattern.charAt(i);
            char wordChar = word.charAt(i);
            if (patternChar != '_' && patternChar != wordChar) {
                return false;
            }
            if (patternChar == '_' && excluded.contains(wordChar)) {
                return false;
            }
        }
        return true;
    }
}
