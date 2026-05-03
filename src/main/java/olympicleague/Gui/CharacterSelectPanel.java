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

    private static final String[] GODS = {
            "Ares", "Zeus", "Poseidon", "Time", "THE GOAT",
            "Artemis", "Hermes", "Apollo", "Talona", "Loki"
    };
    private static final int[] HP_VALUES = {
            1800, 1600, 1700, 1500, 99999,
            1500, 1600, 1400, 1550, 1700
    };
    private static final int[] MP_VALUES = {
            900, 1000, 950, 1100, 99999,
            1000, 950, 1200, 900, 1100
    };
    private static final String[][] SKILLS = {
            {"Spear Thrust", "Aegis Shield", "Wrath of Ares"},
            {"Lion's Strike", "Iron Hide", "Thunder Wrath"},
            {"Tidal Wave", "Ocean's Shield", "Poseidon's Wrath"},
            {"Time Slash", "Temporal Shift", "Chrono Mark"},
            {"Tip of Ballpen", "-", "-"},
            {"Piercing Arrow", "Hunter's Reflex", "Moonlit Mark"},
            {"Swift Strike", "Vanish", "Hermes' Speed"},
            {"Power Chord", "Healing Hymn", "Symphony of Destruction"},
            {"Kit Kit", "Rat Spot", "Talona's Might"},
            {"Rage Bait", "Respawn Shield", "Loki's Hack"}
    };
    private static final String[][] SKILL_DESC = {
            {"380 Base Dmg. Cost: 200MP", "Reduce next hit by 50%. Cost: 250MP", "500 True Dmg. Cost: 500MP"},
            {"350 Base Dmg. Cost: 180MP", "Reduce next hit by 45%. Cost: 220MP", "480 True Dmg. Cost: 480MP"},
            {"370 Base Dmg. Cost: 190MP", "Reduce next hit by 55%. Cost: 240MP", "490 True Dmg. Cost: 490MP"},
            {"360 Base Dmg. Cost: 185MP", "Untargetable 1 turn. Cost: 230MP", "420 Dmg + Mark. Cost: 450MP"},
            {"1,000,000 True Dmg. Cost: 0MP", "-", "-"},
            {"350 Base Dmg. Cost: 175MP", "Evade next attack. Cost: 220MP", "450 Dmg + Mark. Cost: 460MP"},
            {"340 Base Dmg. Cost: 170MP", "Untargetable 1 turn. Cost: 210MP", "Double attack. Cost: 470MP"},
            {"330 Base Dmg. Cost: 165MP", "Heal 300 HP. Cost: 200MP", "400 Dmg + Stun. Cost: 480MP"},
            {"320 Base Dmg. Cost: 160MP", "Dodge 2 turns. Cost: 200MP", "440 Dmg. Cost: 460MP"},
            {"420 Base Dmg. Cost: 220MP", "Reduce next hit by 60%. Cost: 280MP", "450 True Dmg. Cost: 480MP"}
    };
    private static final String[] SPRITE_NAMES = {
            "Achiron", "Heralde", "Orris", "Vor", "SirKhai",
            "Atalyn", "Orven", "Biji", "GoatedKit", "Selwyn"
    };

    private int selectedIndex = 0;

    // update constructor to add grid
    public CharacterSelectPanel(int playerNumber, Runnable onConfirm) {
        this.playerNumber = playerNumber;
        this.onConfirm = onConfirm;
        setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(buildTitleLabel(), BorderLayout.NORTH);
        add(buildRosterGrid(), BorderLayout.WEST);  // ADD
    }

    private JPanel buildRosterGrid() {
        JPanel grid = new JPanel(new GridLayout(3, 4, 8, 8));
        grid.setBackground(Theme.BACKGROUND);
        grid.setPreferredSize(new Dimension(360, 300));
        for (int i = 0; i < NAMES.length; i++) {
            final int idx = i;
            JPanel card = new JPanel(new BorderLayout(4, 4));
            card.setBackground(Theme.PANEL_DARK);
            card.setBorder(BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 1));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            SpriteCanvas mini = new SpriteCanvas(
                    SPRITE_NAMES[i], SpriteLoader.AnimType.IDLE, 48, false, 8
            );
            mini.setOpaque(false);
            JLabel nameLbl = new JLabel(NAMES[i], SwingConstants.CENTER);
            nameLbl.setFont(Theme.SMALL);
            nameLbl.setForeground(Theme.FOREGROUND);
            card.add(mini, BorderLayout.CENTER);
            card.add(nameLbl, BorderLayout.SOUTH);
            card.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    selectedIndex = idx;
                    updateDetail(idx);
                }
                @Override public void mouseEntered(MouseEvent e) { card.setBackground(Theme.PANEL_LIGHT); }
                @Override public void mouseExited(MouseEvent e)  { card.setBackground(selectedIndex == idx ? Theme.PANEL_LIGHT : Theme.PANEL_DARK); }
            });
            grid.add(card);
        }
        return grid;
    }

    private void updateDetail(int idx) { /* stub for now */ }
}
