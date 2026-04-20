package ligaolympica.character;
import java.util.*;

public class Vor extends GameCharacter {
    Scanner scan = new Scanner(System.in);

    public Vor() {
        super("Vor", """
                                              Incarnation of Keith, God of Time - Raised in the realms of time where gods dwell, Vor's mastery over time is unparalleled.
                                              Said to be a legend at birth by Keith, he wields the power of time itself.""",
              1700, 1000, "Skill 1: Time Slash - Slashing time that deals 300 damage and ignores 20% of armor.", "Skill 2: Temporal Shift - Stop time to evade the next attack.", "Gods Gift: Chrono Mark - Mark the target, increasing damage dealt by 25% for 2 turns.");
    }

    @Override
    public void skill1(GameCharacter target) {
        if(skill1Cooldown > 0){
            return;
        }else{
        typewriter("\n" + name + " slashes time with a TIME SLASH!", 30);
        }
        if (this.mana >= 150) {
            this.useMana(150);
            this.skill1Cooldown = 1;

            int baseDamage = 300;
            int damage = randomDamage(baseDamage, 20); // ±20 damage variance

            typewriter("Time itself is slashed!", 30);
            typewriter("Dealt " + damage + " damage to " + target.getName() + "!", 10);

            target.takeDamage(damage);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill2(GameCharacter target) {
        if(skill2Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " activates TEMPORAL SHIFT!", 30);
        }
        if (this.mana >= 120) {
            this.useMana(120);
            this.skill2Cooldown = 2;

            this.untargetable = true;
            this.statusEffectTurns = 1; // Lasts for 1 turn

            typewriter("Time freezes momentarily! " + name + " will evade the next attack!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(skill3Cooldown > 0){
            return;
        }else{
        typewriter("\n" + name + " wields the power of time with CHRONO MARK!", 30);
        }
        if (this.mana >= 500) {
            this.useMana(500);
            this.skill3Cooldown = 5;

            this.statusEffectTurns = 2;
            this.attackBonus = 1.25;

            typewriter("KEITH'S LEGEND! Time is marked on the target - damage increased by 25% for 2 turns!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
            if(skill1Cooldown > 0) {
                typewriter("Time Slash is on cooldown for " + this.skill1Cooldown + " turns.", 10);
            }
            if(skill2Cooldown > 0) {
                typewriter("Temporal Shift is on cooldown for " + this.skill2Cooldown + " turns.", 10);
            }
            if(skill3Cooldown > 0) {
                typewriter("Chrono Mark is on cooldown for " + this.skill3Cooldown + " turns.", 10);
            }

            System.out.println();
            typewriter(name + " - Health: " + health + "|" + maxHealth + ", Mana: " + mana + "/" + maxMana, 10);
            }


    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + name + ":", 10);
        typewriter("1) Time Slash - 300 Base Damage - CD: " + skill1Cooldown, 10);
        typewriter("2) Temporal Shift - Evade Next Attack - CD: " + skill2Cooldown, 10);
        typewriter("3) Chrono Mark - Increase Damage by 25% - CD: " + skill3Cooldown, 10);
        typewriter("0) Escape Battle", 10);

        boolean validChoice = false;
        while (!validChoice) {
            try {
                int choice;
                System.out.print("Enter the number of your choice: ");
                choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        if (skill1Cooldown > 0) {
                            typewriter("Skill is on cooldown for " + skill1Cooldown + " more turns!", 5);
                        } else {
                            skill1(target);
                            validChoice = true;
                        }
                    }
                    case 2 -> {
                        if (skill2Cooldown > 0) {
                            typewriter("Skill is on cooldown for " + skill2Cooldown + " more turns!", 5);
                        } else {
                            skill2(target);
                            validChoice = true;
                        }
                    }
                    case 3 -> {
                        if (skill3Cooldown > 0) {
                            typewriter("Skill is on cooldown for " + skill3Cooldown + " more turns!", 5);
                        } else {
                            skill3(target);
                        }
                    }
                    case 0 -> {
                        typewriter(name + " attempts to flee the battle!", 10);
                        this.hasEscaped = true;
                        validChoice = true;
                        return;
                    }
                    default -> {
                        typewriter("Invalid choice. Please enter 1, 2, or 3.", 5);
                        scan.nextLine(); // clear invalid input
                    }
                }
            } catch (Exception e) {
                typewriter("Invalid input. Please enter a number between 1 and 3.", 5);
                scan.nextLine(); // clear invalid input
            }
        }
    }
}

