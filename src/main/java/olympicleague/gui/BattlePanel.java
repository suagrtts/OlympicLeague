package olympicleague.gui;

import olympicleague.character.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class BattlePanel extends JPanel {

    private final GameCharacter player1;
    private final GameCharacter player2;
    private final boolean       isAI;
    private final Runnable      onBattleEnd;

    // State
    private boolean player1Turn = true;   // true = player1's turn
    private boolean battleOver  = false;
    private int     p1Wins = 0, p2Wins = 0, currentRound = 1;
    private static final int MAX_ROUNDS = 3;

    // GUI widgets
    private AnimatedBar p1HpBar, p1MpBar, p2HpBar, p2MpBar;
    private JTextArea   battleLog;
    private JPanel      skillButtonPanel;
    private JLabel      turnLabel, roundLabel;
    private SpriteCanvas p1Sprite, p2Sprite;
    private ArenaPanel  arenaPanel;

    public BattlePanel(GameCharacter player1, GameCharacter player2, boolean isAI, Runnable onBattleEnd) {
        this.player1     = player1;
        this.player2     = player2;
        this.isAI        = isAI;
        this.onBattleEnd = onBattleEnd;

        setLayout(new BorderLayout(0, 0));
        setBackground(Theme.BG_DEEP);

        add(buildTopBar(),     BorderLayout.NORTH);
        add(buildArena(),      BorderLayout.CENTER);
        add(buildBottomPanel(),BorderLayout.SOUTH);

        // Start first round
        startRound();
    }

    // ── Top stat bars ────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG_CARD);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.alpha(Theme.GOLD, 60)));

        // Player 1 stats (left)
        JPanel p1Stats = buildStatBlock(player1, true);
        // Round / turn info (center)
        JPanel center  = buildCenterInfo();
        // Player 2 stats (right)
        JPanel p2Stats = buildStatBlock(player2, false);

        top.add(p1Stats, BorderLayout.WEST);
        top.add(center,  BorderLayout.CENTER);
        top.add(p2Stats, BorderLayout.EAST);
        return top;
    }

    private JPanel buildStatBlock(GameCharacter gc, boolean isLeft) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        panel.setPreferredSize(new Dimension(220, 80));

        JLabel name = new JLabel(gc.getName());
        name.setFont(Theme.FONT_SUBHEAD);
        name.setForeground(isLeft ? new Color(0xC0, 0x39, 0x2B) : new Color(0x34, 0x98, 0xDB));
        name.setAlignmentX(isLeft ? Component.LEFT_ALIGNMENT : Component.RIGHT_ALIGNMENT);

        AnimatedBar hpBar = new AnimatedBar("HP", Theme.HP_GREEN, gc.getMaxHealth());
        AnimatedBar mpBar = new AnimatedBar("MP", Theme.MP_BLUE,  gc.getMaxMana());
        hpBar.setTarget(gc.getHealth(), gc.getMaxHealth());
        mpBar.setTarget(gc.getMana(),   gc.getMaxMana());
        hpBar.setMaximumSize(new Dimension(200, 22));
        mpBar.setMaximumSize(new Dimension(200, 22));

        panel.add(name);
        panel.add(Box.createVerticalStrut(4));
        panel.add(hpBar);
        panel.add(Box.createVerticalStrut(2));
        panel.add(mpBar);

        if (isLeft) { p1HpBar = hpBar; p1MpBar = mpBar; }
        else         { p2HpBar = hpBar; p2MpBar = mpBar; }

        return panel;
    }

    private JPanel buildCenterInfo() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Theme.BG_CARD);

        roundLabel = new JLabel("Round 1 / 3  —  0-0");
        roundLabel.setFont(Theme.FONT_SMALL);
        roundLabel.setForeground(Theme.GOLD);

        turnLabel = new JLabel(player1.getName() + "'s Turn");
        turnLabel.setFont(Theme.FONT_SKILL);
        turnLabel.setForeground(Theme.TEXT_DIM);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; center.add(roundLabel, gbc);
        gbc.gridy = 1; center.add(turnLabel, gbc);
        return center;
    }

    private ArenaPanel buildArena() {
        p1Sprite = new SpriteCanvas(player1.getName(), SpriteLoader.AnimType.IDLE, 200, false, 8);
        p2Sprite = new SpriteCanvas(player2.getName(), SpriteLoader.AnimType.IDLE, 200, true, 8);

        arenaPanel = new ArenaPanel(p1Sprite, p2Sprite);
        return arenaPanel;
    }

    // ── Bottom: log + skill buttons ──────────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setBackground(Theme.BG_DEEP);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Battle log
        battleLog = new JTextArea(6, 30);
        battleLog.setEditable(false);
        battleLog.setBackground(Theme.BG_CARD);
        battleLog.setForeground(Theme.TEXT_LIGHT);
        battleLog.setFont(Theme.FONT_MONO);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setBorder(BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 50)));
        logScroll.setPreferredSize(new Dimension(320, 120));

        // Skill buttons
        skillButtonPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        skillButtonPanel.setBackground(Theme.BG_DEEP);
        skillButtonPanel.setPreferredSize(new Dimension(280, 120));
        buildSkillButtons(player1);

        bottom.add(logScroll,        BorderLayout.CENTER);
        bottom.add(skillButtonPanel, BorderLayout.EAST);
        return bottom;
    }

    private void buildSkillButtons(GameCharacter actor) {
        skillButtonPanel.removeAll();

        List<Skill> skills = actor.getSkills();
        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);
            final int idx = i;
            JButton btn = new JButton(s.getName() + (s.isReady() ? "" : " (" + s.getCurrentCooldown() + ")"));
            btn.setFont(Theme.FONT_SKILL);
            btn.setBackground(s.isReady() ? Theme.BG_CARD2 : Theme.SKILL_CD);
            btn.setForeground(s.isReady() ? Theme.TEXT_LIGHT : Theme.TEXT_DIM);
            btn.setBorder(BorderFactory.createLineBorder(s.isReady() ? Theme.alpha(Theme.GOLD, 80) : Theme.alpha(Theme.GOLD, 20)));
            btn.setFocusPainted(false);
            btn.setEnabled(s.isReady());
            btn.setToolTipText(s.getDescription());
            btn.addActionListener(e -> onSkillUsed(actor, idx));
            skillButtonPanel.add(btn);
        }

        // Flee button
        JButton fleeBtn = new JButton("🏃 Flee");
        fleeBtn.setFont(Theme.FONT_SKILL);
        fleeBtn.setBackground(new Color(0x30, 0x15, 0x15));
        fleeBtn.setForeground(new Color(0xFF, 0x70, 0x70));
        fleeBtn.setBorder(BorderFactory.createLineBorder(new Color(0x80, 0x20, 0x20)));
        fleeBtn.setFocusPainted(false);
        fleeBtn.addActionListener(e -> {
            log(actor.getName() + " fled the battle!");
            endBattle();
        });
        skillButtonPanel.add(fleeBtn);

        skillButtonPanel.revalidate();
        skillButtonPanel.repaint();
    }

    // ── Battle Logic ─────────────────────────────────────────────────────────
    private void startRound() {
        player1.resetForNewRound();
        player2.resetForNewRound();
        player1Turn = true;
        battleOver  = false;

        updateBars();
        updateRoundLabel();
        log("=== Round " + currentRound + " ===");
        log(player1.getName() + " vs " + player2.getName());
        setPlayerTurn(true);
    }

    private void setPlayerTurn(boolean isPlayer1) {
        player1Turn = isPlayer1;
        GameCharacter actor = isPlayer1 ? player1 : player2;
        turnLabel.setText(actor.getName() + "'s Turn");

        if (isPlayer1) {
            // Player 1's turn — show skill buttons
            buildSkillButtons(player1);
            setSkillButtonsEnabled(true);
        } else {
            // Player 2's turn
            if (isAI) {
                setSkillButtonsEnabled(false);
                // AI takes turn after a short delay
                Timer aiTimer = new Timer(800, e -> {
                    doAITurn();
                });
                aiTimer.setRepeats(false);
                aiTimer.start();
            } else {
                // PvP: show player 2 skill buttons
                buildSkillButtons(player2);
                setSkillButtonsEnabled(true);
            }
        }
    }

    private void onSkillUsed(GameCharacter actor, int skillIndex) {
        if (battleOver) return;
        GameCharacter target = actor == player1 ? player2 : player1;

        // Disable buttons during action
        setSkillButtonsEnabled(false);

        // Update cooldowns for actor
        actor.updateTurnEffects();

        // Check stun
        if (actor.isStunned()) {
            log(actor.getName() + " is stunned and cannot act!");
            actor.setStunned(false);
        } else {
            // Execute skill
            String result = actor.takeTurn(target, skillIndex);
            log(actor.getName() + ": " + result);
        }

        // Restore some mana
        actor.restoreMana(150);
        target.restoreMana(150);

        updateBars();

        // Animate
        boolean actorIsP1 = actor == player1;
        SpriteCanvas attackerSprite = actorIsP1 ? p1Sprite : p2Sprite;
        SpriteCanvas defenderSprite = actorIsP1 ? p2Sprite : p1Sprite;

        attackerSprite.setAnimType(SpriteLoader.AnimType.ATTACK);
        Timer resetAttacker = new Timer(600, e -> {
            attackerSprite.setAnimType(SpriteLoader.AnimType.IDLE);
        });
        resetAttacker.setRepeats(false);
        resetAttacker.start();

        if (!target.isAlive()) {
            defenderSprite.setAnimType(SpriteLoader.AnimType.HURT);
            Timer afterDeath = new Timer(700, e -> handleRoundEnd());
            afterDeath.setRepeats(false);
            afterDeath.start();
        } else {
            defenderSprite.setAnimType(SpriteLoader.AnimType.HURT);
            Timer resetDefender = new Timer(400, e -> defenderSprite.setAnimType(SpriteLoader.AnimType.IDLE));
            resetDefender.setRepeats(false);
            resetDefender.start();

            // Switch turns
            Timer switchTimer = new Timer(700, e -> setPlayerTurn(!player1Turn));
            switchTimer.setRepeats(false);
            switchTimer.start();
        }
    }

    private void doAITurn() {
        if (battleOver) return;

        player2.updateTurnEffects();

        if (player2.isStunned()) {
            log(player2.getName() + " is stunned!");
            player2.setStunned(false);
        } else {
            String result = player2.autoTakeTurn(player1);
            log(player2.getName() + ": " + result);
        }

        player1.restoreMana(150);
        player2.restoreMana(150);
        updateBars();

        p2Sprite.setAnimType(SpriteLoader.AnimType.ATTACK);
        Timer resetAttack = new Timer(600, e -> p2Sprite.setAnimType(SpriteLoader.AnimType.IDLE));
        resetAttack.setRepeats(false);
        resetAttack.start();

        if (!player1.isAlive()) {
            p1Sprite.setAnimType(SpriteLoader.AnimType.HURT);
            Timer afterDeath = new Timer(700, e -> handleRoundEnd());
            afterDeath.setRepeats(false);
            afterDeath.start();
        } else {
            p1Sprite.setAnimType(SpriteLoader.AnimType.HURT);
            Timer resetHurt = new Timer(400, e -> p1Sprite.setAnimType(SpriteLoader.AnimType.IDLE));
            resetHurt.setRepeats(false);
            resetHurt.start();

            // Switch back to player 1
            Timer switchTimer = new Timer(700, e -> setPlayerTurn(true));
            switchTimer.setRepeats(false);
            switchTimer.start();
        }
    }

    private void handleRoundEnd() {
        String winner = "";
        if (player1.isAlive() && !player2.isAlive()) {
            p1Wins++;
            winner = player1.getName() + " wins Round " + currentRound + "!";
        } else if (!player1.isAlive() && player2.isAlive()) {
            p2Wins++;
            winner = player2.getName() + " wins Round " + currentRound + "!";
        } else {
            winner = "Round " + currentRound + " — Draw!";
        }
        log(winner);
        log("Score: " + p1Wins + " - " + p2Wins);
        updateRoundLabel();

        currentRound++;

        if (p1Wins >= 2 || p2Wins >= 2 || currentRound > MAX_ROUNDS) {
            // Match over
            Timer endTimer = new Timer(1200, e -> {
                if (p1Wins > p2Wins)      log("🏆 " + player1.getName() + " WINS THE MATCH!");
                else if (p2Wins > p1Wins) log("🏆 " + player2.getName() + " WINS THE MATCH!");
                else                       log("Match ends in a Draw!");
                endBattle();
            });
            endTimer.setRepeats(false);
            endTimer.start();
        } else {
            // Next round
            Timer nextRound = new Timer(1500, e -> startRound());
            nextRound.setRepeats(false);
            nextRound.start();
        }
    }

    private void endBattle() {
        battleOver = true;
        setSkillButtonsEnabled(false);
        // Show "Return to Menu" button
        skillButtonPanel.removeAll();
        JButton menuBtn = CharacterSelectPanel.makeButton("🏠 Main Menu", Theme.GOLD);
        menuBtn.addActionListener(e -> onBattleEnd.run());
        skillButtonPanel.add(menuBtn);
        skillButtonPanel.revalidate();
        skillButtonPanel.repaint();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private void updateBars() {
        p1HpBar.setTarget(player1.getHealth(), player1.getMaxHealth());
        p1MpBar.setTarget(player1.getMana(),   player1.getMaxMana());
        p2HpBar.setTarget(player2.getHealth(), player2.getMaxHealth());
        p2MpBar.setTarget(player2.getMana(),   player2.getMaxMana());
    }

    private void updateRoundLabel() {
        roundLabel.setText("Round " + Math.min(currentRound, MAX_ROUNDS) + " / " + MAX_ROUNDS
            + "   " + p1Wins + " - " + p2Wins);
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            battleLog.append(msg + "\n");
            battleLog.setCaretPosition(battleLog.getDocument().getLength());
        });
    }

    private void setSkillButtonsEnabled(boolean enabled) {
        for (Component c : skillButtonPanel.getComponents()) {
            if (c instanceof JButton btn) {
                // Flee button is always enabled
                if (btn.getText().contains("Flee")) { btn.setEnabled(true); continue; }
                // Only enable skill buttons that are ready
                if (enabled) {
                    // Check cooldown
                    int idx = skillButtonPanel.getComponentZOrder(btn);
                    GameCharacter actor = player1Turn ? player1 : player2;
                    if (idx < actor.getSkills().size()) {
                        btn.setEnabled(actor.getSkills().get(idx).isReady());
                    }
                } else {
                    btn.setEnabled(false);
                }
            }
        }
    }

    // ── Arena Panel with background image ───────────────────────────────────
    private static class ArenaPanel extends JPanel {
        private final SpriteCanvas p1Sprite, p2Sprite;
        private BufferedImage bg;

        ArenaPanel(SpriteCanvas p1Sprite, SpriteCanvas p2Sprite) {
            this.p1Sprite = p1Sprite;
            this.p2Sprite = p2Sprite;

            int bgIdx = (int)(Math.random() * 4);
            bg = SpriteLoader.getBattleground(bgIdx, 900, 360);

            setLayout(null);
            setBackground(Theme.BG_DEEP);

            add(p1Sprite);
            add(p2Sprite);
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            if (bg != null) {
                Graphics2D g = (Graphics2D) g0.create();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                // Subtle dark overlay for contrast
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.dispose();
            }
        }

        @Override
        public void doLayout() {
            int w = getWidth(), h = getHeight();
            int spriteSize = 200;

            // Player 1 — left side, bottom-aligned
            p1Sprite.setBounds(80, h - spriteSize - 20, spriteSize, spriteSize);
            // Player 2 — right side, bottom-aligned
            p2Sprite.setBounds(w - 80 - spriteSize, h - spriteSize - 20, spriteSize, spriteSize);
        }
    }
}
