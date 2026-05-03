package gui;

import character.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CharacterSelectPanel extends JPanel {

    private static final String[] NAMES = {
            "Achiron", "Heralde", "Orris", "Vor", "SirKhai",
            "Atalyn", "Orven", "Biji", "GoatedKit", "Selwyn"
    };

    private final int playerNumber;
    private final Runnable onConfirm;
    private GameCharacter selectedCharacter;

    public CharacterSelectPanel(int playerNumber, Runnable onConfirm) {
        this.playerNumber = playerNumber;
        this.onConfirm = onConfirm;
        setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(buildTitleLabel(), BorderLayout.NORTH);
    }

    private JLabel buildTitleLabel() {
        JLabel title = new JLabel("PLAYER " + playerNumber + " — SELECT CHARACTER", SwingConstants.CENTER);
        title.setFont(Theme.TITLE);
        title.setForeground(Theme.TITLE_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        return title;
    }

    public GameCharacter getSelectedCharacter() { return selectedCharacter; }
}