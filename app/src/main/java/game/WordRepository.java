package game;

/**
 * Legacy wrapper kept for compatibility with older builds or cached class names.
 * <p>
 * The actual word storage now lives in {@link Words}. This class simply extends it
 * so any existing references to {@code WordRepository} continue to compile.
 */
public class WordRepository extends Words {
    // no-op: all logic is in the parent class
}
