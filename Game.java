package ligaolympica;
import ligaolympica.ui.Menu;

public class Game {
    private final Menu menu = new Menu();

    public void start() {
        typewriter("Welcome to Liga Olympica!", 30);
        menu.showMainMenu();
    }

    public void typewriter(String text, int delay) {
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
}