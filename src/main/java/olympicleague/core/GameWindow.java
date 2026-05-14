package olympicleague.core;

import olympicleague.assets.Theme;
import olympicleague.audio.MenuMusic;
import olympicleague.character.*;
import olympicleague.gui.panels.*;
import olympicleague.audio.BattleSound;

import javax.swing.*;
import java.awt.*;



public class GameWindow extends JFrame {

    private static final String CARD_MENU      = "MENU";
    private static final String CARD_SELECT1   = "SELECT1";
    private static final String CARD_SELECT2   = "SELECT2";
    private static final String CARD_BATTLE    = "BATTLE";
    private static final String CARD_ROSTER    = "ROSTER";
    private static final String CARD_ARCADE    = "ARCADE";
    private static final String CARD_DIFF      = "DIFFICULTY";

    private final CardLayout cards = new CardLayout();
    private final JPanel     root  = new JPanel(cards);

    private GameCharacter p1, p2;
    private boolean       battleIsAI;
    private String        arcadeDifficulty;

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
        BattleSound.stopGrandWinner();
        BattleSound.stopBattleBgm();
        MenuMusic.start();
        replaceCard(CARD_MENU, new MainMenuPanel(
                this::startPvP,
                this::startPvE,
                this::showArcadeDifficulty,
                this::showRoster
        ));
        cards.show(root, CARD_MENU);
    }

    private void startPvP() {
        MenuMusic.start();
        battleIsAI = false;
        replaceCard(CARD_SELECT1, new CharacterSelectPanel(
                "Player 1",
                gc -> { p1 = gc; showP2Select(); },
                this::showMenu
        ));
        cards.show(root, CARD_SELECT1);
    }

    private void showP2Select() {
        replaceCard(CARD_SELECT2, new CharacterSelectPanel(
                "Player 2",
                gc -> { p2 = gc; launchBattle(); },
                this::startPvP
        ));
        cards.show(root, CARD_SELECT2);
    }

    private void startPvE() {
        MenuMusic.start();
        battleIsAI = true;
        replaceCard(CARD_SELECT1, new CharacterSelectPanel(
                "Player 1",
                gc -> { p1 = gc; p2 = randomChar(p1.getName()); launchBattle(); },
                this::showMenu
        ));
        cards.show(root, CARD_SELECT1);
    }

    private void showArcadeDifficulty() {
        MenuMusic.start();
        replaceCard(CARD_DIFF, new ArcadeDifficultyPanel(
                diff -> { arcadeDifficulty = diff; startArcade(); },
                this::showMenu
        ));
        cards.show(root, CARD_DIFF);
    }

    private void startArcade() {
        MenuMusic.start();
        replaceCard(CARD_SELECT1, new CharacterSelectPanel(
                "Player 1 — Arcade",
                gc -> { p1 = gc; launchArcade(); },
                this::showArcadeDifficulty
        ));
        cards.show(root, CARD_SELECT1);
    }

    private void launchArcade() {
        MenuMusic.start();
        GameCharacter[] opponents = buildArcadeOpponents(arcadeDifficulty, p1.getName());
        replaceCard(CARD_ARCADE, new ArcadePanel(p1, opponents, arcadeDifficulty, this::showMenu, this::showMenu));
        cards.show(root, CARD_ARCADE);
    }

    private void launchBattle() {
        MenuMusic.stop();
        replaceCard(CARD_BATTLE, new BattlePanel(p1, p2, battleIsAI, this::showPostBattle));
        cards.show(root, CARD_BATTLE);
    }

    private void showRoster() {
        MenuMusic.start();
        replaceCard(CARD_ROSTER, new RosterPanel(this::showMenu));
        cards.show(root, CARD_ROSTER);
    }

    private void showPostBattle(BattleResult result) {
        SwingUtilities.invokeLater(() -> {
            MenuMusic.start();

            JPanel panel = buildPostBattlePanel(result);

            replaceCard("POSTBATTLE", panel);
            cards.show(root, "POSTBATTLE");
        });
    }

    private JPanel buildPostBattlePanel(BattleResult result) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_DEEP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14,14,14,14);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;


        String titleText = switch (result) {
            case PLAYER_WIN  -> "🏆 Victory!";
            case PLAYER_LOSE -> "💀 Defeat!";
            case PLAYER_FLED -> "🏃 You Fled!";
        };

        JLabel lbl = new JLabel(titleText, JLabel.CENTER);
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

    // ── Delegates to CharacterSelectPanel to avoid duplication ───────────────
    public static GameCharacter makeChar(String name) {
        return CharacterSelectPanel.makeChar(name);
    }

    private GameCharacter[] buildArcadeOpponents(String diff, String playerName) {
        String[] pool = {
                "Achiron", "Atalyn", "Heralde", "Vor", "Orris",
                "Orven", "Biji", "Selwyn", "GoatedKit", "TinySwords"
        };
        java.util.List<String> list = new java.util.ArrayList<>(java.util.Arrays.asList(pool));
        list.remove(playerName);
        java.util.Collections.shuffle(list);
        int count = switch (diff) {
            case "EASY"   -> Math.min(4, list.size());
            case "MEDIUM" -> Math.min(6, list.size());
            default       -> list.size();
        };
        GameCharacter[] arr = new GameCharacter[count];
        for (int i = 0; i < count; i++) arr[i] = makeChar(list.get(i));
        return arr;
    }

    private GameCharacter randomChar(String excludeName) {
        String[] names = {
                "Achiron", "Atalyn", "Heralde", "Vor", "Orris",
                "Orven", "Biji", "Selwyn", "GoatedKit", "TinySwords"
        };
        java.util.List<String> list = new java.util.ArrayList<>(java.util.Arrays.asList(names));
        if (excludeName != null) list.remove(excludeName);
        return makeChar(list.get((int)(Math.random() * list.size())));
    }
}