package olympicleague.battle;

import java.util.Scanner;
import olympicleague.character.GameCharacter;
import olympicleague.character.Skill;
import olympicleague.util.GameUtils;

/**
 * Console-mode battle engine (used by the legacy terminal UI).
 * The GUI path uses {@link olympicleague.gui.BattlePanel} instead.
 */
public class Battle {

    private static final int MAX_ROUNDS = 3;
    private static final int MAX_TURNS  = 40;
    private static final int MANA_REGEN = 150;

    private final GameCharacter player1;
    private final GameCharacter player2;
    private final Scanner scan;

    public Battle(GameCharacter player1, GameCharacter player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.scan    = new Scanner(System.in);
    }

    /** PvP convenience — equivalent to {@code startBattle(false)}. */
    public boolean startBattle() {
        return startBattle(false);
    }

    /**
     * Best-of-3 match.
     *
     * @param player2IsComputer {@code true} if player 2 is AI-controlled
     * @return {@code false} if a player fled mid-match, {@code true} otherwise
     */
    public boolean startBattle(boolean player2IsComputer) {
        GameUtils.typewriter("\nMatch Start (Best of 3): "
                + player1.getName() + " vs " + player2.getName(), 0);

        int wins1 = 0, wins2 = 0;

        for (int round = 1; round <= MAX_ROUNDS && wins1 < 2 && wins2 < 2; round++) {
            player1.resetForNewRound();
            player2.resetForNewRound();

            GameUtils.typewriter("\n=== Round " + round + " ===", 10);

            // Odd rounds: P1 goes first. Even rounds: P2 goes first.
            GameCharacter attacker = (round % 2 == 1) ? player1 : player2;
            GameCharacter defender = (attacker == player1) ? player2 : player1;

            boolean fled = runTurnLoop(attacker, defender, player2IsComputer);
            if (fled) return false;

            boolean p1alive = player1.isAlive();
            boolean p2alive = player2.isAlive();

            if (p1alive && !p2alive) {
                wins1++;
                GameUtils.typewriter("\n>>> " + player1.getName() + " wins Round " + round + "!", 10);
            } else if (!p1alive && p2alive) {
                wins2++;
                GameUtils.typewriter("\n>>> " + player2.getName() + " wins Round " + round + "!", 10);
            } else {
                GameUtils.typewriter("\n>>> Round " + round + " ended in a draw! No points awarded.", 10);
            }

            GameUtils.typewriter("Score: " + player1.getName() + " " + wins1
                    + " - " + wins2 + " " + player2.getName(), 10);
        }

        announceMatchWinner(wins1, wins2);
        System.out.println();
        return true;
    }

    /** Arcade mode — single-round fight, no round resets. */
    public boolean startArcadeBattle() {
        return startArcadeBattle(true);
    }

    public boolean startArcadeBattle(boolean player2IsComputer) {
        GameUtils.typewriter("\nBattle Start: " + player1.getName()
                + " vs " + player2.getName(), 0);

        boolean fled = runTurnLoop(player1, player2, player2IsComputer);
        if (fled) return false;

        if (!player1.isAlive() && !player2.isAlive()) {
            GameUtils.typewriter("\nThe battle ended in a draw!", 10);
        } else {
            GameCharacter winner = player1.isAlive() ? player1 : player2;
            printWinnerBanner(winner.getName());
        }
        return true;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private boolean runTurnLoop(GameCharacter first, GameCharacter second,
                                 boolean player2IsComputer) {
        GameCharacter attacker = first;
        GameCharacter defender = second;

        for (int turn = 1; turn <= MAX_TURNS && attacker.isAlive() && defender.isAlive(); turn++) {
            System.out.println("\n--- Turn " + turn + " ---");
            System.out.println(attacker.getName() + "'s turn!");

            attacker.updateTurnEffects();

            if (attacker.isStunned()) {
                GameUtils.typewriter(attacker.getName() + " is stunned and cannot act!", 30);
                attacker.setStunned(false);
            } else if (attacker == player2 && player2IsComputer) {
                GameUtils.typewriter("Thinking...", 15);
                GameUtils.typewriter(attacker.autoTakeTurn(defender), 20);
            } else {
                handleHumanTurn(attacker, defender);
                if (attacker.hasEscaped()) {
                    GameUtils.typewriter("\n" + attacker.getName() + " has fled the battle!", 10);
                    return true;
                }
            }

            System.out.println();
            GameUtils.typewriter(
                    player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth()
                    + " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(),
                    10);

            attacker.restoreMana(MANA_REGEN);
            defender.restoreMana(MANA_REGEN);

            GameCharacter tmp = attacker;
            attacker = defender;
            defender = tmp;
        }
        return false;
    }

    private void handleHumanTurn(GameCharacter player, GameCharacter enemy) {
        displayStats(player);
        GameUtils.typewriter("\nChoose a skill for " + player.getName() + ":", 10);

        int i = 1;
        for (Skill s : player.getSkills()) {
            GameUtils.typewriter(i + ") " + s.getName()
                    + " - " + s.getDescription()
                    + " (CD: " + s.getCurrentCooldown() + ")", 10);
            i++;
        }
        GameUtils.typewriter("0) Escape Battle", 10);

        while (true) {
            System.out.print("Enter the number of your choice: ");
            try {
                int choice = Integer.parseInt(scan.nextLine().trim());

                if (choice == 0) {
                    player.setEscaped(true);
                    return;
                }

                if (choice < 1 || choice > player.getSkills().size()) {
                    GameUtils.typewriter("Invalid choice. Please select an available option.", 5);
                    continue;
                }

                Skill chosen = player.getSkills().get(choice - 1);
                if (!chosen.isReady()) {
                    GameUtils.typewriter("Skill is on cooldown for "
                            + chosen.getCurrentCooldown() + " more turns!", 5);
                } else {
                    GameUtils.typewriter(player.takeTurn(enemy, choice - 1), 20);
                    return;
                }
            } catch (NumberFormatException e) {
                GameUtils.typewriter("Invalid input. Please enter a valid number.", 5);
            }
        }
    }

    private void displayStats(GameCharacter c) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.getName())
          .append(" - HP: ").append(c.getHealth()).append("/").append(c.getMaxHealth())
          .append(" | MP: ").append(c.getMana()).append("/").append(c.getMaxMana());

        StringBuilder cooldowns = new StringBuilder();
        for (Skill s : c.getSkills()) {
            if (!s.isReady()) {
                if (cooldowns.length() == 0) cooldowns.append("  Cooldowns: ");
                cooldowns.append(s.getName()).append("(").append(s.getCurrentCooldown()).append(") ");
            }
        }

        GameUtils.typewriter(sb.toString(), 10);
        if (cooldowns.length() > 0) GameUtils.typewriter(cooldowns.toString(), 10);
    }

    private void announceMatchWinner(int wins1, int wins2) {
        if (wins1 > wins2)      printWinnerBanner(player1.getName() + " wins the match!");
        else if (wins2 > wins1) printWinnerBanner(player2.getName() + " wins the match!");
        else GameUtils.typewriter("\nThe match ended without a decisive winner.", 10);
    }

    private void printWinnerBanner(String text) {
        GameUtils.typewriter("\n╔════════════════════════════╗", 0);
        GameUtils.typewriter("    " + text, 0);
        GameUtils.typewriter("╚════════════════════════════╝", 0);
    }
}