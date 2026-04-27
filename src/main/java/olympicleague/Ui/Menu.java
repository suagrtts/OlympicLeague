package olympicleague.ui;

import java.util.*;
import Battle.Battle;
import Character.Achiron;
import Character.Atalyn;
import Character.Biji;
import Character.GameCharacter;
import Character.GoatedKit;
import Character.Heralde;
import Character.Orris;
import Character.Orven;
import Character.Selwyn;
import Character.SirKhai;
import Character.Vor;

public class Menu {
    private final Scanner scanner;
    private final Random random;
    private final GameCharacter achiron;
    private final GameCharacter atalyn;
    private final GameCharacter vor;
    private final GameCharacter heralde;
    private final GameCharacter orris;
    private final GameCharacter orven;
    private final GameCharacter biji;
    private final GameCharacter selwyn;
    private final GameCharacter goatedKit;
    private final GameCharacter sirKhai;

    public Menu() {
        scanner = new Scanner(System.in);
        random = new Random();
        achiron = new Achiron();
        atalyn = new Atalyn();
        vor = new Vor();
        heralde = new Heralde();
        orris = new Orris();
        orven = new Orven();
        biji = new Biji();
        selwyn = new Selwyn();
        goatedKit = new GoatedKit();
        sirKhai = new SirKhai();
    }

    private boolean startBattle(GameCharacter player1, GameCharacter player2) {
        Battle battle = new Battle(player1, player2);
        return battle.startBattle(false);
    }

    public void showMainMenu() {
        boolean running = true;
        while (running) {
            try {
                typewriter("Main Menu", 30);
                typewriter("1. Start PvP Game", 30);
                typewriter("2. Start PvE Game", 30);
                typewriter("3. Start Arcade Mode", 30);
                typewriter("4. Show Characters", 30);
                typewriter("5. Exit", 30);
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> startGame();
                    case 2 -> startGameAI();
                    case 3 -> startArcadeMode();
                    case 4 -> showCharacterInfo();
                    case 5 -> {
                        typewriter("Exiting game. Goodbye!", 30);
                        running = false;
                    }
                    default -> typewriter("Invalid choice. Please enter a number between 1 and 5.", 30);
                }
            } catch (Exception e) {
                typewriter("Invalid input. Please enter a valid number.", 30);
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private void showCharacterInfo() {
        while(true){
            try {
                typewriter("Character List:", 30);
                typewriter("1. Achiron", 30);
                typewriter("2. Atalyn", 30);
                typewriter("3. Heralde", 30);
                typewriter("4. Vor", 30);
                typewriter("5. Orris", 30);
                typewriter("6. Orven", 30);
                typewriter("7. Biji", 30);
                typewriter("8. Selwyn", 30);
                typewriter("9. Goated Kit", 30);
                typewriter("0. Return to Main Menu", 30);
                System.out.print("Select a character to view details (1-9): ");

                int pick = scanner.nextInt();
                scanner.nextLine();

                switch (pick) {
                    case 1 -> {
                        typewriter("You selected Achiron!", 30);
                        achiron.showInfo();
                    }
                    case 2 -> {
                        typewriter("You selected Atalyn!", 30);
                        atalyn.showInfo();
                    }
                    case 3 -> {
                        typewriter("You selected Heralde!", 30);
                        heralde.showInfo();
                    }
                    case 4 -> {
                        typewriter("You selected Vor!", 30);
                        vor.showInfo();
                    }
                    case 5 -> {
                        typewriter("You selected Orris!", 30);
                        orris.showInfo();
                    }
                    case 6 -> {
                        typewriter("You selected Orven!", 30);
                        orven.showInfo();
                    }
                    case 7 -> {
                        typewriter("You selected Biji!", 30);
                        biji.showInfo();
                    }
                    case 8 -> {
                        typewriter("You selected Selwyn!", 30);
                        selwyn.showInfo();
                    }
                    case 9 -> {
                        typewriter("You selected Goated Kit!", 30);
                        goatedKit.showInfo();
                    }
                    case 10 -> {
                        typewriter("You selected THE GOAT, Sir Khai", 20);
                        sirKhai.showInfo();
                    }
                    case 0 -> {
                        typewriter("Returning to Main Menu.", 30);
                        return;
                    }
                    default -> typewriter("Invalid character choice.", 30);
                }
            } catch (InputMismatchException e) {
                typewriter("Invalid input. Please enter a number between 0 and 9.", 30);
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private void startGame() {
        GameCharacter player1 = selectCharacter(1);
        if (player1 == null) return;

        GameCharacter player2 = selectCharacter(2);
        if (player2 == null) return;

        typewriter("\nPlayer 1 selected: ", 10);
        typewriter(player1.getName(), 10);
        typewriter("\nPlayer 2 selected: ", 10);
        typewriter(player2.getName(), 10);

        boolean battleCompleted = startBattle(player1, player2);

        if (!battleCompleted) {
            typewriter("\nBattle escaped! Returning to main menu...", 30);
        }
    }

    private GameCharacter selectCharacter(int playerNumber) {
        boolean isValidChoice = false;
        while (!isValidChoice) {
            try {
                typewriter("\nPlayer " + playerNumber + ", select your character:", 5);
                typewriter("1. Achiron", 5);
                typewriter("2. Atalyn", 5);
                typewriter("3. Heralde", 5);
                typewriter("4. Vor", 5);
                typewriter("5. Orris", 5);
                typewriter("6. Orven", 5);
                typewriter("7. Biji", 5);
                typewriter("8. Selwyn", 5);
                typewriter("9. Goated Kit", 5);
                typewriter("0. Exit to Main Menu", 5);
                typewriter("Choose your character (1-9): ", 5);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        return new Achiron();
                    }
                    case 2 -> {
                        return new Atalyn();
                    }
                    case 3 -> {
                        return new Heralde();
                    }
                    case 4 -> {
                        return new Vor();
                    }
                    case 5 -> {
                        return new Orris();
                    }
                    case 6 -> {
                        return new Orven();
                    }
                    case 7 -> {
                        return new Biji();
                    }
                    case 8 -> {
                        return new Selwyn();
                    }
                    case 9 -> {
                        return new GoatedKit();
                    }
                    case 10 ->{
                        return new SirKhai();
                    }
                    case 0 -> {
                        typewriter("Returning to Main Menu.", 5);
                        return null;
                    }
                    default -> {
                        typewriter("Invalid choice. Please enter a number between 1 and 9.", 5);
                    }
                }
            } catch (Exception e) {
                typewriter("Invalid input. Please enter a number between 0 and 9.", 5);
                scanner.nextLine(); // Clear the invalid input
            }
        }
        return null; // This line should never be reached
    }

    private void startGameAI() {
        GameCharacter player = selectCharacter(1); // Player selection
        if (player == null) return;

        // AI selects a random character
        GameCharacter ai = null;
        int aiPick = random.nextInt(9) + 1;
        switch (aiPick) {
            case 1 -> ai = new Achiron();
            case 2 -> ai = new Atalyn();
            case 3 -> ai = new Heralde();
            case 4 -> ai = new Vor();
            case 5 -> ai = new Orris();
            case 6 -> ai = new Orven();
            case 7 -> ai = new Biji();
            case 8 -> ai = new Selwyn();
            case 9 -> ai = new GoatedKit();
        }

        assert ai != null; // AI selection is guaranteed by random range
        typewriter("\nPlayer selected: ", 10);
        typewriter(player.getName(), 10);
        typewriter("\nAI selected: ", 10);
        typewriter(ai.getName(), 10);

        // Start battle
        Battle battle = new Battle(player, ai);
        boolean battleCompleted = battle.startBattle(true);

        if (!battleCompleted) {
            typewriter("\nPlayer escaped! Returning to main menu...", 30);
        }
    }

    private void startArcadeMode() {
        GameCharacter player = selectCharacter(1);
        boolean isValid = false;
        while(!isValid){
            try {
                typewriter("Select Difficulty Level:", 30);
                typewriter("1. Easy", 30);
                typewriter("2. Medium", 30);
                typewriter("3. Hard", 30);
                typewriter("0. Return to Main Menu", 30);
                System.out.print("Choose an option (1-3): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        typewriter("You selected Easy difficulty.", 30);
                        GameCharacter[] easyOpponents = {new Vor(), new Orris(), new Orven(), new Achiron()};
                        shuffleArray(easyOpponents);
                        arcadeBattle(player, easyOpponents);
                        isValid = true;
                    }
                    case 2 -> {
                        typewriter("You selected Medium difficulty.", 30);
                        GameCharacter[] mediumOpponents = {new Achiron(), new Vor(), new Orris(), new Orven(), new Biji(), new Selwyn()};
                        shuffleArray(mediumOpponents);
                        arcadeBattle(player, mediumOpponents);
                        isValid = true;
                    }
                    case 3 -> {
                        typewriter("You selected Hard difficulty.", 30);
                        GameCharacter[] hardOpponents = {new Heralde(), new Achiron(), new Vor(), new Atalyn(), new Orris(), new Orven(), new Biji(), new Selwyn(), new GoatedKit()};
                        shuffleArray(hardOpponents);
                        arcadeBattle(player, hardOpponents);
                        isValid = true;
                    }
                    case 0 -> {
                        typewriter("Returning to Main Menu.", 30);
                        return;
                    }
                    default -> typewriter("Invalid choice. Please enter a number between 1 and 3.", 30);
                }
            } catch (Exception e) {
                typewriter("Invalid input. Please enter a valid number.", 30);
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private void shuffleArray(GameCharacter[] array) {
        List<GameCharacter> list = Arrays.asList(array);
        Collections.shuffle(list);
    }

    private void arcadeBattle(GameCharacter player, GameCharacter[] opponents) {
        int defeatedOpponents = 0;

        typewriter("\n=== ARCADE MODE START ===", 50);
        typewriter("Defeat all " + opponents.length + " opponents to win!", 30);
        typewriter("=============================\n", 50);

        for (int i = 0; i < opponents.length; i++) {
            if (player.getHealth() <= 0) {
                typewriter("\n*** GAME OVER ***", 50);
                typewriter("You were defeated after beating " + defeatedOpponents + " opponent(s).", 30);
                typewriter("Better luck next time!", 30);
                return; // Exit arcade mode
            }

            typewriter("\n--- Battle " + (i + 1) + " of " + opponents.length + " ---", 40);
            typewriter("Opponent: " + opponents[i].getName(), 30);
            typewriter("Your HP: " + player.getHealth() + "/" + player.getMaxHealth(), 30);
            typewriter("\nPress Enter to continue...", 30);
            scanner.nextLine();

            Battle battle = new Battle(player, opponents[i]);
            boolean battleCompleted = battle.startArcadeBattle(true);

            if (!battleCompleted) {
                typewriter("\n*** You escaped from Arcade Mode! ***", 50);
                typewriter("You defeated " + defeatedOpponents + " opponent(s) before escaping.", 30);
                typewriter("Returning to main menu...", 30);
                return;  // Exit arcade mode and return to menu
            }

            if (player.getHealth() > 0) {
                defeatedOpponents++;
                typewriter("\n*** Victory! ***", 50);
                typewriter("Opponents defeated: " + defeatedOpponents + "/" + opponents.length, 30);

                //Restore some health between battles
                if (i < opponents.length - 1) { // Not the last battle
                    int healAmount = (int)((double)player.getMaxHealth() * 0.75); // Heal 75% of max health
                    player.heal(healAmount);
                    typewriter("You recovered " + healAmount + " HP!", 30);
                    typewriter("Current HP: " + player.getHealth() + "/" + player.getMaxHealth() + "\n", 30);
                }
            }
        }
        if (player.getHealth() > 0) {
            typewriter("\n╔════════════════════════════╗", 0);
            typewriter("║   ARCADE MODE COMPLETE!    ║", 0);
            typewriter("╚════════════════════════════╝", 0);
            typewriter("You defeated all " + opponents.length + " opponents!", 30);
            typewriter("Final HP: " + player.getHealth() + "/" + player.getMaxHealth(), 30);
            typewriter("\nPress Enter to return to main menu...", 30);
            scanner.nextLine();
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