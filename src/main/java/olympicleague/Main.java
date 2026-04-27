import Ui.GameIntro;

public class Main {

    public static void main(String[] args) {
        GameIntro.showIntro();

        Game game = new Game();
        game.start();
    }
}