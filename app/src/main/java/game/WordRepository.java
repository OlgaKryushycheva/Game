package game;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Репозиторій слів із короткими підказками для раундів гри.
 * ВАЖЛИВО: назва класу повинна залишатися WordRepository (ASCII),
 * щоби збігалася з іменем файлу та уникати помилок компіляції.
 */
public class WordRepository {
    private final List<WordEntry> words = Arrays.asList(
            new WordEntry("КОМП'ЮТЕР", "електронна машина для обробки даних"),
            new WordEntry("ІНТЕЛЕКТ", "здатність мислити, розуміти й навчатися"),
            new WordEntry("ПРОГРАМА", "набір інструкцій, які виконує комп'ютер"),
            new WordEntry("АЛГОРИТМ", "точна послідовність дій для розв’язання задачі"),
            new WordEntry("БІБЛІОТЕКА", "набір готових класів або функцій"),
            new WordEntry("СТРУКТУРА", "організована сукупність елементів даних"),
            new WordEntry("КОНСТРУКТОР", "метод ініціалізації об'єкта"),
            new WordEntry("СЕРІАЛІЗАЦІЯ", "перетворення об'єкта у потік байтів"),
            new WordEntry("ПАРАЛЕЛЬНІСТЬ", "одночасне виконання кількох обчислень"),
            new WordEntry("ІНКАПСУЛЯЦІЯ", "приховування деталей реалізації всередині класу"),
            new WordEntry("ГАЛАКТИКА", "величезна система зірок і туманностей"),
            new WordEntry("КОСМІЧНИЙ", "пов'язаний з космосом або космічними польотами"),
            new WordEntry("ПРИГОДА", "неочікувана подія або ризикована подорож"),
            new WordEntry("ДЖАВА", "об'єктно-орієнтована мова програмування"),
            new WordEntry("СИМУЛЯЦІЯ", "модель, що відтворює роботу системи")
    );

    public WordEntry randomEntry() {
        int index = ThreadLocalRandom.current().nextInt(words.size());
        WordEntry entry = words.get(index);
        return new WordEntry(entry.word().toUpperCase(Locale.forLanguageTag("uk")), entry.hint());
    }

    public String guessByPattern(String pattern, List<Character> excluded) {
        for (WordEntry entry : words) {
            String word = entry.word();
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
