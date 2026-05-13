package olympicleague.util;

/**
 * Shared console-output helpers for the terminal UI.
 * All methods are static — no instantiation needed.
 */
public final class GameUtils {

    private GameUtils() {} // utility class

    /** Prints each character with a delay, then appends a newline. */
    public static void typewriter(String text, int delayMs) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            if (delayMs > 0) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        System.out.println();
    }

    /** Prints text centred within an 80-character terminal width. */
    public static void centeredPrint(String text, int delayMs) {
        int padding = Math.max(0, (80 - text.length()) / 2);
        System.out.print(" ".repeat(padding));
        typewriter(text, delayMs);
    }

    /** Prints a decorative separator line of '═' characters. */
    public static void separator(int width) {
        System.out.println("═".repeat(width));
    }

    /** Clamps {@code value} between {@code min} and {@code max} inclusive. */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}