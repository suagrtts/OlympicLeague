import javax.swing.JFrame;

public class GameWindow extends JFrame{
    public static void main(String[] args) {
        JFrame frame = new GameWindow();
        frame.setTitle("Liga Olympica");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
