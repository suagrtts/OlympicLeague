package olympicleague.gui;

import olympicleague.character.GameCharacter;
import javax.swing.*;
import java.awt.*;

/**
 * ArcadePanel — manages the sequence of Arcade Mode battles.
 * Player fights all opponents one by one; partial HP carries over.
 * Between fights, heals 50% max HP.
 */
public class ArcadePanel extends JPanel {

    private final GameCharacter   player;
    private final GameCharacter[] opponents;
    private final String          difficulty;
    private final Runnable        onWin;
    private final Runnable        onLoseOrExit;

    private int  currentOpponentIndex = 0;
    private JPanel contentWrapper;
    private CardLayout contentCards;

    public ArcadePanel(GameCharacter player, GameCharacter[] opponents,
                       String difficulty, Runnable onWin, Runnable onLoseOrExit) {
        this.player       = player;
        this.opponents    = opponents;
        this.difficulty   = difficulty;
        this.onWin        = onWin;
        this.onLoseOrExit = onLoseOrExit;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_DEEP);

        contentCards = new CardLayout();
        contentWrapper = new JPanel(contentCards);
        contentWrapper.setBackground(Theme.BG_DEEP);
        add(contentWrapper, BorderLayout.CENTER);

        showBriefing();
    }

    private void showBriefing() {
        MenuMusic.start();
        JPanel brief = new JPanel(new GridBagLayout());
        brief.setBackground(Theme.BG_DEEP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 20, 10, 20);

        JLabel title = new JLabel("ARCADE MODE — " + difficulty, JLabel.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.GOLD);

        JLabel info = new JLabel("Defeat " + opponents.length + " opponents to win!", JLabel.CENTER);
        info.setFont(Theme.FONT_BODY);
        info.setForeground(Theme.TEXT_DIM);

        JLabel charInfo = new JLabel("Your fighter: " + player.getName(), JLabel.CENTER);
        charInfo.setFont(Theme.FONT_SUBHEAD);
        charInfo.setForeground(Theme.TEXT_LIGHT);

        JButton start = CharacterSelectPanel.makeButton("⚔ BEGIN", Theme.GOLD);
        start.addActionListener(e -> launchNextBattle());

        JButton exit = CharacterSelectPanel.makeButton("← Main Menu", Theme.TEXT_DIM);
        exit.addActionListener(e -> onLoseOrExit.run());

        brief.add(title,    gbc);
        brief.add(info,     gbc);
        brief.add(charInfo, gbc);
        brief.add(Box.createVerticalStrut(20), gbc);
        brief.add(start,    gbc);
        brief.add(exit,     gbc);

        contentWrapper.add(brief, "BRIEF");
        contentCards.show(contentWrapper, "BRIEF");
    }

    private void launchNextBattle() {
        if (currentOpponentIndex >= opponents.length) {
            showVictory();
            return;
        }

        MenuMusic.stop();
        GameCharacter opponent = opponents[currentOpponentIndex];
        // Reset opponent for fresh fight; player keeps current HP
        opponent.resetForNewRound();

        // P1 sprite faces right (no flip for most), P2 sprite faces left (flipped)
        // Pass isAI=true so opponent is controlled by AI
        BattlePanel battlePanel = new BattlePanel(player, opponent, true, this::onBattleEnd) {
            // Override to NOT reset player HP between arcade fights
        };

        String key = "BATTLE_" + currentOpponentIndex;
        contentWrapper.add(battlePanel, key);
        contentCards.show(contentWrapper, key);
    }

    private void onBattleEnd() {
        MenuMusic.start();
        if (!player.isAlive() || player.getHealth() <= 0) {
            showGameOver();
            return;
        }
        // Player survived
        currentOpponentIndex++;
        if (currentOpponentIndex >= opponents.length) {
            showVictory();
        } else {
            // Heal 50% between battles
            int healAmt = player.getMaxHealth() / 2;
            player.heal(healAmt);
            showIntermission(healAmt);
        }
    }

    private void showIntermission(int healAmt) {
        MenuMusic.start();
        JPanel inter = new JPanel(new GridBagLayout());
        inter.setBackground(Theme.BG_DEEP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 20, 10, 20);

        JLabel win = new JLabel("Victory! (" + currentOpponentIndex + "/" + opponents.length + ")", JLabel.CENTER);
        win.setFont(Theme.FONT_TITLE);
        win.setForeground(Theme.HP_GREEN);

        JLabel hp = new JLabel("Recovered " + healAmt + " HP  |  Current: "
            + player.getHealth() + "/" + player.getMaxHealth(), JLabel.CENTER);
        hp.setFont(Theme.FONT_BODY);
        hp.setForeground(Theme.TEXT_DIM);

        JLabel next = new JLabel("Next opponent: " + opponents[currentOpponentIndex].getName(), JLabel.CENTER);
        next.setFont(Theme.FONT_SUBHEAD);
        next.setForeground(Theme.GOLD);

        JButton cont = CharacterSelectPanel.makeButton("⚔ Continue", Theme.GOLD);
        cont.addActionListener(e -> launchNextBattle());

        inter.add(win,  gbc);
        inter.add(hp,   gbc);
        inter.add(next, gbc);
        inter.add(cont, gbc);

        contentWrapper.add(inter, "INTER_" + currentOpponentIndex);
        contentCards.show(contentWrapper, "INTER_" + currentOpponentIndex);
    }

    private void showVictory() {
        MenuMusic.start();
        JPanel vic = new JPanel(new GridBagLayout());
        vic.setBackground(Theme.BG_DEEP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(12, 20, 12, 20);

        JLabel trophy = new JLabel("🏆", JLabel.CENTER);
        trophy.setFont(new Font("Serif", Font.PLAIN, 60));

        JLabel title = new JLabel("ARCADE COMPLETE!", JLabel.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.GOLD);

        JLabel sub = new JLabel(player.getName() + " has conquered all " + opponents.length + " opponents!", JLabel.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_DIM);

        JLabel finalHp = new JLabel("Final HP: " + player.getHealth() + "/" + player.getMaxHealth(), JLabel.CENTER);
        finalHp.setFont(Theme.FONT_SUBHEAD);
        finalHp.setForeground(Theme.HP_GREEN);

        JButton menu = CharacterSelectPanel.makeButton("🏠 Main Menu", Theme.GOLD);
        menu.addActionListener(e -> onWin.run());

        vic.add(trophy,  gbc);
        vic.add(title,   gbc);
        vic.add(sub,     gbc);
        vic.add(finalHp, gbc);
        vic.add(menu,    gbc);

        contentWrapper.add(vic, "VICTORY");
        contentCards.show(contentWrapper, "VICTORY");
    }

    private void showGameOver() {
        MenuMusic.start();
        JPanel go = new JPanel(new GridBagLayout());
        go.setBackground(Theme.BG_DEEP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(12, 20, 12, 20);

        JLabel title = new JLabel("DEFEATED", JLabel.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(new Color(0xC0, 0x39, 0x2B));

        JLabel sub = new JLabel("You fell after defeating " + currentOpponentIndex
            + " / " + opponents.length + " opponents.", JLabel.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_DIM);

        JButton menu = CharacterSelectPanel.makeButton("🏠 Main Menu", Theme.TEXT_DIM);
        menu.addActionListener(e -> onLoseOrExit.run());

        go.add(title, gbc);
        go.add(sub,   gbc);
        go.add(menu,  gbc);

        contentWrapper.add(go, "GAMEOVER");
        contentCards.show(contentWrapper, "GAMEOVER");
    }
}
