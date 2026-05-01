

import Ui.*;

public class Main {

    public static void main(String[] args) {
        ui.GameIntro.showIntro();

        Game game = new Game();
        game.start();
    }
}