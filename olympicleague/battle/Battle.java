package ligaolympica.battle;

import ligaolympica.character.GameCharacter;

public class Battle {
    private final GameCharacter player1;
    private final GameCharacter player2;

    public Battle(GameCharacter player1, GameCharacter player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public boolean startBattle() {
        return startBattle(false);
    }

    // New overload: if player2IsComputer is true, player2 will use autoTakeTurn
    public boolean startBattle(boolean player2IsComputer) {
        // Best of 3 rounds: first to 2 wins
        System.out.println("\nMatch Start (Best of 3): " + player1.getName() + " vs " + player2.getName());

        int wins1 = 0;
        int wins2 = 0;
        int round = 1;

        final int maxRounds = 3;

        while (wins1 < 2 && wins2 < 2 && round <= maxRounds) {
            // Reset characters for the round
            player1.resetForNewRound();
            player2.resetForNewRound();

            typewriter("\n=== Round " + round + " ===", 10);

            GameCharacter attacker;
            GameCharacter defender;
            // Alternate who starts each round for fairness
            if (round % 2 == 1) {
                attacker = player1;
                defender = player2;
            } else {
                attacker = player2;
                defender = player1;
            }

            int turn = 1;
            int maxTurns = 40;

            while (attacker.isAlive() && defender.isAlive() && turn <= maxTurns) {
                System.out.println("\n--- Turn " + turn + " ---");
                System.out.println(attacker.getName() + "'s turn!");


                if (attacker.isStunned()) {
                    typewriter(attacker.getName() + " is stunned and cannot act!", 30);
                    attacker.setStunned(false); // Clear stun after missing turn

                    // Still update cooldowns and other effects
                    attacker.updateTurnEffects();
                    attacker.displayStats();
                    System.out.println();

                    typewriter(player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth()
                        + " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(), 10);

                    attacker.restoreMana(150);
                    defender.restoreMana(150);

                    // Swap attacker/defender
                    GameCharacter temp = attacker;
                    attacker = defender;
                    defender = temp;

                    turn++;
                    continue; // Skip to next turn
                }

                // Update cooldowns and buffs at the START of the character's turn
                attacker.updateTurnEffects();

                // Character acts: if defender is player2 and player2IsComputer is true, use autoTakeTurn for that character when they act
                if (attacker == player2 && player2IsComputer) {
                    attacker.autoTakeTurn(defender);
                } else {
                    attacker.takeTurn(defender);

                    if (attacker.hasEscaped()) {
                        typewriter("\n" + attacker.getName() + " has fled the battle!", 10);
                        return false;  // ← Return false to indicate escape
                    }
                }

                // Show status for the acting character
                attacker.displayStats();
                System.out.println();

                typewriter(player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth() + " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(), 10);
                // Both characters regain some mana each turn
                attacker.restoreMana(150);
                defender.restoreMana(150);

                // Swap attacker/defender
                GameCharacter temp = attacker;
                attacker = defender;
                defender = temp;

                turn++;
            }

            // Determine round winner
            if (player1.isAlive() && !player2.isAlive()) {
                wins1++;
                typewriter("\n>>> " + player1.getName() + " wins Round " + round + "!", 10);
            } else if (!player1.isAlive() && player2.isAlive()) {
                wins2++;
                typewriter("\n>>> " + player2.getName() + " wins Round " + round + "!", 10);
            } else {
                typewriter("\n>>> Round " + round + " ended in a draw! No points awarded.", 10);
            }

            typewriter("Score: " + player1.getName() + " " + wins1 + " - " + wins2 + " " + player2.getName(), 10);

            round++;
        }

        // Announce match result
        if (wins1 > wins2) {
            typewriter("\n╔════════════════════════════╗", 0);
            typewriter("    " + player1.getName() + " wins the match!        ", 0);
            typewriter("╚════════════════════════════╝", 0);
        } else if (wins2 > wins1) {
            typewriter("\n╔════════════════════════════╗", 0);
            typewriter("    " + player2.getName() + " wins the match!        ", 0);
            typewriter("╚════════════════════════════╝", 0);
        } else {
            typewriter("\nThe match ended without a decisive winner.", 10);
        }
        System.out.println();
        return true;
    }

    public boolean startArcadeBattle() {
        return startArcadeBattle(true);
    }

    public boolean startArcadeBattle(boolean player2IsComputer) {
        System.out.println("\nBattle Start: " + player1.getName() + " vs " + player2.getName());

        GameCharacter attacker = player1;
        GameCharacter defender = player2;
        int turn = 1;
        int maxTurns = 40;

        while (player1.isAlive() && player2.isAlive() && turn <= maxTurns) {
            System.out.println("\n--- Turn " + turn + " ---");
            System.out.println(attacker.getName() + "'s turn!");

            if (attacker.isStunned()) {
                typewriter(attacker.getName() + " is stunned and cannot act!", 30);
                attacker.setStunned(false); // Clear stun after missing turn

                // Still update cooldowns and other effects
                attacker.updateTurnEffects();
                attacker.displayStats();
                System.out.println();

                typewriter(player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth()
                    + " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(), 10);

                attacker.restoreMana(150);
                defender.restoreMana(150);

                // Swap attacker/defender
                GameCharacter temp = attacker;
                attacker = defender;
                defender = temp;

                turn++;
                continue; // Skip to next turn
            }

            attacker.updateTurnEffects();

            // Character acts: if defender is player2 and player2IsComputer is true, use autoTakeTurn for that character when they act
            if (attacker == player2 && player2IsComputer) {
                attacker.autoTakeTurn(defender);
            } else {
                attacker.takeTurn(defender);

                if (attacker.hasEscaped()) {
                    typewriter("\n" + attacker.getName() + " has fled the battle!", 10);
                    return false;  // ← Return false to indicate escape
                }
            }

            // Show status
            attacker.displayStats();
            System.out.println();

            typewriter(player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth() + " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(), 10);
            // Both characters regain some mana each turn
            attacker.restoreMana(150);
            defender.restoreMana(150);

            // Swap attacker/defender
            GameCharacter temp = attacker;
            attacker = defender;
            defender = temp;

            turn++;
        }

        if (turn > maxTurns) {
            typewriter("\nThe battle ended in a draw!", 10);
        } else if (player1.isAlive()) {
            typewriter("\n╔════════════════════════════╗", 0);
            typewriter("      " + player1.getName() + " wins!        ", 0);
            typewriter("╚════════════════════════════╝", 0);
            System.out.println();
        } else {
            typewriter("\n╔════════════════════════════╗", 0);
            typewriter("      " + player2.getName() + " wins!        ", 0);
            typewriter("╚════════════════════════════╝", 0);
            System.out.println();
        }
        return true; //This can be reached if the battle ends normally
    }

    public void typewriter(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
}