package olympicleague.util;

import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * BattleLogStream — redirects System.out prints into a JTextArea for the battle log.
 * (Previously an empty stub)
 */
public class BattleLogStream extends OutputStream {

    private final JTextArea target;
    private final StringBuilder buffer = new StringBuilder();

    public BattleLogStream(JTextArea target) {
        this.target = target;
    }

    /** Install this stream as System.out so battle log messages appear in the UI. */
    public void install() {
        System.setOut(new PrintStream(this, true));
    }

    @Override
    public void write(int b) {
        buffer.append((char) b);
        if (b == '\n') flush();
    }

    @Override
    public void flush() {
        final String text = buffer.toString();
        buffer.setLength(0);
        SwingUtilities.invokeLater(() -> {
            target.append(text);
            target.setCaretPosition(target.getDocument().getLength());
        });
    }
}
