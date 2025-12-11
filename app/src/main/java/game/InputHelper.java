package game;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Обгортка над Scanner, що виводить дружнє повідомлення, якщо вхідні дані недоступні
 * (наприклад, Gradle запущено без інтерактивної консолі).
 */
public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Зчитує рядок або завершує програму з поясненням, якщо ввід недоступний.
     */
    public String nextLineOrExit(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        try {
            if (!scanner.hasNextLine()) {
                System.out.println();
                System.out.println("Ввід недоступний. Запустіть задачу у звичайному терміналі та вводьте команди вручну.");
                throw new InputUnavailableException();
            }
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Ввід недоступний. Запустіть задачу у звичайному терміналі з можливістю вводити текст.");
            throw new InputUnavailableException();
        }
    }

    /**
     * Триммінг безпеки для меню/імен.
     */
    public String nextTrimmedOrExit(String prompt) {
        return nextLineOrExit(prompt).trim();
    }
}
