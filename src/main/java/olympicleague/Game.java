import Ui.*;
import Util.GameUtils;

public class Game {
    private final Menu menu = new Menu();

    public void start() {
        // We use GameUtils to call the method now
        GameUtils.typewriter("Welcome to Liga Olympica!", 30);
        menu.showMainMenu();
    }

}