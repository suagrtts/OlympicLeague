package gui;

import character.*;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private static final String CARD_MENU    = "MENU";
    private static final String CARD_SELECT1 = "SELECT1";
    private static final String CARD_SELECT2 = "SELECT2";
    private static final String CARD_BATTLE  = "BATTLE";
    private static final String CARD_ROSTER  = "ROSTER";

    private final CardLayout cards = new CardLayout();
    private final JPanel     root  = new JPanel(cards);

    private GameCharacter p1, p2;
    private boolean       battleIsAI;

    public GameWindow() {
        super("Liga Olympica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(940, 720);
        setMinimumSize(new Dimension(820, 620));
        setLocationRelativeTo(null);
        root.setBackground(Theme.BG_DEEP);
        setContentPane(root);
        showMenu();
        setVisible(true);
    }

    public void showMenu() {
        replaceCard(CARD_MENU, new MainMenuPanel(this::startPvP, this::startPvE, this::showRoster));
        cards.show(root, CARD_MENU);
    }

    private void startPvP() {
        battleIsAI = false;
        replaceCard(CARD_SELECT1, new CharacterSelectPanel("Player 1", gc -> {
            p1 = gc;
            replaceCard(CARD_SELECT2, new CharacterSelectPanel("Player 2", gc2 -> {
                p2 = gc2;
                launchBattle();
            }));
            cards.show(root, CARD_SELECT2);
        }));
        cards.show(root, CARD_SELECT1);
    }

    private void startPvE() {
        battleIsAI = true;
        replaceCard(CARD_SELECT1, new CharacterSelectPanel("Player 1", gc -> {
            p1 = gc;
            p2 = randomChar();
            launchBattle();
        }));
        cards.show(root, CARD_SELECT1);
    }

    private void launchBattle() {
        replaceCard(CARD_BATTLE, new BattlePanel(p1, p2, battleIsAI, this::showPostBattle));
        cards.show(root, CARD_BATTLE);
    }

    private void showRoster() {
        replaceCard(CARD_ROSTER, new RosterPanel(this::showMenu));
        cards.show(root, CARD_ROSTER);
    }

    private void showPostBattle() {
        SwingUtilities.invokeLater(() -> {
            JPanel panel = buildPostBattlePanel();
            replaceCard("POSTBATTLE", panel);
            cards.show(root, "POSTBATTLE");
        });
    }

    private JPanel buildPostBattlePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_DEEP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14,14,14,14);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lbl = new JLabel("Battle Over!", JLabel.CENTER);
        lbl.setFont(Theme.FONT_TITLE);
        lbl.setForeground(Theme.GOLD);
        p.add(lbl, gbc);

        gbc.gridy = 1;
        JLabel sub = new JLabel("What would you like to do?", JLabel.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_DIM);
        p.add(sub, gbc);

        gbc.gridy = 2;
        JButton again = CharacterSelectPanel.makeButton("⚔  Play Again", Theme.GOLD);
        again.addActionListener(e -> { if (battleIsAI) startPvE(); else startPvP(); });
        p.add(again, gbc);

        gbc.gridy = 3;
        JButton menu = CharacterSelectPanel.makeButton("🏠  Main Menu", Theme.TEXT_DIM);
        menu.addActionListener(e -> showMenu());
        p.add(menu, gbc);

        return p;
    }

    private void replaceCard(String name, JPanel panel) {
        root.add(panel, name);
    }

    private GameCharacter randomChar() {
        String[] names = CharacterSelectPanel.NAMES;
        return switch (names[(int)(Math.random() * names.length)]) {
            case "Atalyn"     -> new Atalyn();
            case "Heralde"    -> new Heralde();
            case "TinySwords" -> new TinySwords();
            case "Orris"      -> new Orris();
            case "Orven"      -> new Orven();
            case "Biji"       -> new Biji();
            case "Selwyn"     -> new Selwyn();
            case "GoatedKit"  -> new GoatedKit();
            case "Skeleton"   -> new Skeleton();
            case "EvilWizard" -> new EvilWizard();
            case "SirKhai"    -> new SirKhai();
            default           -> new Atalyn();
        };
    }
}