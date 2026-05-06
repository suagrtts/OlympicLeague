package olympicleague.gui;

import olympicleague.character.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RosterPanel extends JPanel {

    private final Runnable onBack;
    private int selectedIdx = 0;

    // Detail widgets
    private JLabel detailName, detailHp, detailMp, detailGod;
    private JLabel[] skillLabels = new JLabel[3];
    private JLabel[] skillDescLabels = new JLabel[3];
    private SpriteCanvas detailSprite;
    private JPanel cardGrid;

    public RosterPanel(Runnable onBack) {
        this.onBack = onBack;
        setBackground(Theme.BG_DEEP);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        updateDetail(0);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton backBtn = CharacterSelectPanel.makeButton("← Back", Theme.TEXT_DIM);
        backBtn.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("CHARACTER ROSTER", JLabel.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.GOLD);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(14, 0));
        center.setOpaque(false);
        center.add(buildGrid(),   BorderLayout.WEST);
        center.add(buildDetail(), BorderLayout.CENTER);
        return center;
    }

    private JPanel buildGrid() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(340, 0));

        cardGrid = new JPanel(new GridLayout(3, 3, 6, 6));
        cardGrid.setOpaque(false);

        for (int i = 0; i < CharacterSelectPanel.NAMES.length; i++) {
            cardGrid.add(buildCard(i));
        }
        wrapper.add(cardGrid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCard(int idx) {
        JPanel card = new JPanel(new BorderLayout(2, 2));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 60)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        SpriteCanvas mini = new SpriteCanvas(CharacterSelectPanel.NAMES[idx], SpriteLoader.AnimType.IDLE, 80, false, 8);
        mini.setOpaque(false);

        JLabel name = new JLabel(CharacterSelectPanel.NAMES[idx], JLabel.CENTER);
        name.setFont(Theme.FONT_SMALL);
        name.setForeground(Theme.TEXT_LIGHT);
        name.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        card.add(mini, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { selectedIdx = idx; updateDetail(idx); refreshHighlights(); }
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(Theme.BG_CARD2); }
            @Override public void mouseExited(MouseEvent e)  { card.setBackground(selectedIdx == idx ? Theme.BG_CARD2 : Theme.BG_CARD); }
        });
        return card;
    }

    private void refreshHighlights() {
        Component[] cs = cardGrid.getComponents();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] instanceof JPanel c) {
                c.setBackground(i == selectedIdx ? Theme.BG_CARD2 : Theme.BG_CARD);
                c.setBorder(BorderFactory.createLineBorder(i == selectedIdx ? Theme.GOLD : Theme.alpha(Theme.GOLD, 60), i == selectedIdx ? 2 : 1));
            }
        }
    }

    private JPanel buildDetail() {
        JPanel detail = new JPanel();
        detail.setBackground(Theme.BG_CARD);
        detail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 80)),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));

        detailSprite = new SpriteCanvas(CharacterSelectPanel.NAMES[0], SpriteLoader.AnimType.IDLE, 160, false, 8);
        detailSprite.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailSprite.setMaximumSize(new Dimension(160, 160));

        detailName = styledLabel("", Theme.FONT_HEADER, Theme.GOLD);
        detailGod  = styledLabel("", Theme.FONT_BODY,   Theme.TEXT_DIM);
        detailHp   = styledLabel("", Theme.FONT_SKILL,  Theme.HP_GREEN);
        detailMp   = styledLabel("", Theme.FONT_SKILL,  Theme.MP_BLUE);

        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        statsRow.setOpaque(false); statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(new JLabel("HP:") {{ setFont(Theme.FONT_SKILL); setForeground(Theme.TEXT_DIM); }});
        statsRow.add(detailHp);
        statsRow.add(new JLabel("  MP:") {{ setFont(Theme.FONT_SKILL); setForeground(Theme.TEXT_DIM); }});
        statsRow.add(detailMp);

        JLabel skillsHdr = styledLabel("SKILLS", Theme.FONT_SKILL, Theme.GOLD_DIM);
        JPanel skillsPanel = new JPanel(); skillsPanel.setOpaque(false);
        skillsPanel.setLayout(new BoxLayout(skillsPanel, BoxLayout.Y_AXIS));
        skillsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 3; i++) {
            skillLabels[i]     = styledLabel("", Theme.FONT_SKILL, Theme.TEXT_LIGHT);
            skillDescLabels[i] = styledLabel("", Theme.FONT_SMALL, Theme.TEXT_DIM);
            skillsPanel.add(skillLabels[i]);
            skillsPanel.add(skillDescLabels[i]);
            skillsPanel.add(Box.createVerticalStrut(4));
        }

        detail.add(detailSprite);
        detail.add(Box.createVerticalStrut(10));
        detail.add(detailName);
        detail.add(detailGod);
        detail.add(Box.createVerticalStrut(8));
        detail.add(statsRow);
        detail.add(Box.createVerticalStrut(10));
        detail.add(skillsHdr);
        detail.add(Box.createVerticalStrut(4));
        detail.add(skillsPanel);
        return detail;
    }

    private JLabel styledLabel(String text, Font f, Color c) {
        JLabel l = new JLabel(text); l.setFont(f); l.setForeground(c);
        l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }

    private static final String[] GODS = {
        "Ares","Zeus","Poseidon","Time","Artemis","Hermes","Apollo","Talona","Loki"
    };
    private static final int[] HP = {1800,1600,1700,1500,1500,1600,1400,1550,1700};
    private static final int[] MP = {900,1000,950,1100,1000,950,1200,900,1100};
    private static final String[][] SKILLS = {
        {"Spear Thrust","Aegis Shield","Wrath of Ares"},
        {"Lion's Strike","Iron Hide","Thunder Wrath"},
        {"Tidal Wave","Ocean's Shield","Poseidon's Wrath"},
        {"Time Slash","Temporal Shift","Chrono Mark"},
        {"Piercing Arrow","Hunter's Reflex","Moonlit Mark"},
        {"Swift Strike","Vanish","Hermes' Speed"},
        {"Power Chord","Healing Hymn","Symphony of Destruction"},
        {"Kit Kit","Rat Spot","Talona's Might"},
        {"Rage Bait","Respawn Shield","Loki's Hack"}
    };
    private static final String[][] SKILL_DESC = {
        {"380 Base Dmg","Shield 50% next hit","500 True Dmg"},
        {"350 Base Dmg","Shield 45% next hit","480 True Dmg"},
        {"370 Base Dmg","Shield 55% next hit","490 True Dmg"},
        {"360 Base Dmg","Untargetable 1 turn","420 Dmg + Mark"},
        {"350 Base Dmg","Evade next attack","450 Dmg + Mark"},
        {"340 Base Dmg","Untargetable 1 turn","Double attack"},
        {"330 Base Dmg","Heal 300 HP","400 Dmg + Stun"},
        {"320 Base Dmg","Dodge 2 turns","440 Dmg"},
        {"420 Base Dmg","Shield 60% next hit","450 True Dmg"}
    };

    private void updateDetail(int idx) {
        detailSprite.setCharacter(CharacterSelectPanel.NAMES[idx], SpriteLoader.AnimType.IDLE);
        detailName.setText(CharacterSelectPanel.NAMES[idx]);
        detailGod .setText("God: " + GODS[idx]);
        detailHp  .setText(String.valueOf(HP[idx]));
        detailMp  .setText(String.valueOf(MP[idx]));
        for (int i = 0; i < 3; i++) {
            skillLabels[i]    .setText("  " + (i+1) + ". " + SKILLS[idx][i]);
            skillDescLabels[i].setText("       " + SKILL_DESC[idx][i]);
        }
        repaint();
    }
}
