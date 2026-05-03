package Util;

public class GameUtils {

    // Notice the word "static". This means we don't need to create a "new GameUtils()"
    // object to use it. We can just call GameUtils.typewriter(...) directly!
    public static void typewriter(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    // You can also move those other cool print methods from Main.java here!
    public static void centerPrint(String text, int delay) {
        int width = 80;
        int padding = (width - text.length()) / 2;
        for (int i = 0; i < padding; i++) System.out.print(" ");
        typewriter(text, delay);
    }
}