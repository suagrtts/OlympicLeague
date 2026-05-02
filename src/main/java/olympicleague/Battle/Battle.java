package Battle;

import Character.GameCharacter;
import Character.Skill;
import java.util.Scanner;

public class Battle {
    private final GameCharacter player1;
    private final GameCharacter player2;
    private final Scanner scan; // Added Scanner to the Battle class

    public Battle(GameCharacter player1, GameCharacter player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.scan = new Scanner(System.in);
    }

    public boolean startBattle() {
        return startBattle(false);
    }

    // Best of 3 rounds: first to 2 wins
    public boolean startBattle(boolean player2IsComputer) {
        System.out.println("\nMatch Start (Best of 3): " + player1.getName() + " vs " + player2.getName());

        int wins1 = 0;
        int wins2 = 0;
        int round = 1;
        final int maxRounds = 3;

        while (wins1 < 2 && wins2 < 2 && round <= maxRounds) {
            player1.resetForNewRound();
            player2.resetForNewRound();

            typewriter("\n=== Round " + round + " ===", 10);

            GameCharacter attacker;
            GameCharacter defender;

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

                // 1. Update cooldowns and statuses at the start of the turn
                attacker.updateTurnEffects();

                // 2. Check Stun
                if (attacker.isStunned()) {
                    typewriter(attacker.getName() + " is stunned and cannot act!", 30);
                    attacker.setStunned(false); // Clear stun
                } else {
                    // 3. Take Action
                    if (attacker == player2 && player2IsComputer) {
                        typewriter("Thinking...", 15);
                        String aiLog = attacker.autoTakeTurn(defender);
                        typewriter(aiLog, 20);
                    } else {
                        handleHumanTurn(attacker, defender);
                        if (attacker.hasEscaped()) {
                            typewriter("\n" + attacker.getName() + " has fled the battle!", 10);
                            return false;
                        }
                    }
                }

                // Show HP at the end of the action
                System.out.println();
                typewriter(player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth() +
                        " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(), 10);

                // Mana regen
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

            attacker.updateTurnEffects();

            if (attacker.isStunned()) {
                typewriter(attacker.getName() + " is stunned and cannot act!", 30);
                attacker.setStunned(false);
            } else {
                if (attacker == player2 && player2IsComputer) {
                    typewriter("Thinking...", 15);
                    String aiLog = attacker.autoTakeTurn(defender);
                    typewriter(aiLog, 20);
                } else {
                    handleHumanTurn(attacker, defender);
                    if (attacker.hasEscaped()) {
                        typewriter("\n" + attacker.getName() + " has fled the battle!", 10);
                        return false;
                    }
                }
            }

            System.out.println();
            typewriter(player1.getName() + " - HP: " + player1.getHealth() + "/" + player1.getMaxHealth() +
                    " | " + player2.getName() + " - HP: " + player2.getHealth() + "/" + player2.getMaxHealth(), 10);

            attacker.restoreMana(150);
            defender.restoreMana(150);

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
        return true;
    }

    // --- NEW HELPER METHODS MOVED FROM THE OLD CHARACTERS ---

    private void handleHumanTurn(GameCharacter player, GameCharacter enemy) {
        displayStats(player);
        typewriter("\nChoose a skill for " + player.getName() + ":", 10);

        int i = 1;
        for (Skill s : player.getSkills()) {
            typewriter(i + ") " + s.getName() + " - " + s.getDescription() + " (CD: " + s.getCurrentCooldown() + ")", 10);
            i++;
        }
        typewriter("0) Escape Battle", 10);

        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Enter the number of your choice: ");
            try {
                int choice = Integer.parseInt(scan.nextLine());

                if (choice == 0) {
                    player.setEscaped(true);
                    validChoice = true;
                } else if (choice > 0 && choice <= player.getSkills().size()) {
                    int skillIndex = choice - 1;
                    Skill chosenSkill = player.getSkills().get(skillIndex);

                    if (!chosenSkill.isReady()) {
                        typewriter("Skill is on cooldown for " + chosenSkill.getCurrentCooldown() + " more turns!", 5);
                    } else {
                        // This executes the skill and prints the lore/math result!
                        String combatLog = player.takeTurn(enemy, skillIndex);
                        typewriter(combatLog, 20);
                        validChoice = true;
                    }
                } else {
                    typewriter("Invalid choice. Please select an available option.", 5);
                }
            } catch (NumberFormatException e) {
                typewriter("Invalid input. Please enter a valid number.", 5);
            }
        }
    }

    private void displayStats(GameCharacter c) {
        StringBuilder stats = new StringBuilder();
        stats.append(c.getName()).append(" - HP: ").append(c.getHealth()).append("/").append(c.getMaxHealth())
                .append(" | MP: ").append(c.getMana()).append("/").append(c.getMaxMana()).append("\n");

        boolean hasCooldowns = false;
        for (Skill skill : c.getSkills()) {
            if (!skill.isReady()) {
                if (!hasCooldowns) {
                    stats.append("Cooldowns: ");
                    hasCooldowns = true;
                }
                stats.append(skill.getName()).append("(").append(skill.getCurrentCooldown()).append(") ");
            }
        }
        if (hasCooldowns) {
            typewriter(stats.toString(), 10);
        } else {
            // Just print the HP/MP line
            typewriter(c.getName() + " - HP: " + c.getHealth() + "/" + c.getMaxHealth() + " | MP: " + c.getMana() + "/" + c.getMaxMana(), 10);
        }
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