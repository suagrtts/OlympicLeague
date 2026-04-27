package olympicleague.character;
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
        if(getSkill1Cooldown() > 0){
            return;
        }else{
        typewriter("\n" + getName() + " slashes time with a TIME SLASH!", 30);
        }
        if (getMana() >= 150) {
            this.useMana(150);
            setSkill1Cooldown(1);

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
        if(getSkill2Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " activates TEMPORAL SHIFT!", 30);
        }
        if (getMana() >= 120) {
            this.useMana(120);
            setSkill2Cooldown(2);

            setUntargetable(true);
            setStatusEffectTurns(1); // Lasts for 1 turn

            typewriter("Time freezes momentarily! " + getName() + " will evade the next attack!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(getSkill3Cooldown() > 0){
            return;
        }else{
        typewriter("\n" + getName() + " wields the power of time with CHRONO MARK!", 30);
        }
        if (getMana() >= 500) {
            this.useMana(500);
            setSkill3Cooldown(5);

            setStatusEffectTurns(2);
            setAttackBonus(1.25);

            typewriter("KEITH'S LEGEND! Time is marked on the target - damage increased by 25% for 2 turns!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
            if(getSkill1Cooldown() > 0) {
                typewriter("Time Slash is on cooldown for " + getSkill1Cooldown() + " turns.", 10);
            }
            if(getSkill2Cooldown() > 0) {
                typewriter("Temporal Shift is on cooldown for " + getSkill2Cooldown() + " turns.", 10);
            }
            if(getSkill3Cooldown() > 0) {
                typewriter("Chrono Mark is on cooldown for " + getSkill3Cooldown() + " turns.", 10);
            }

            System.out.println();
            typewriter(getName() + " - Health: " + getHealth() + "|" + getMaxHealth() + ", Mana: " + getMana() + "/" + getMaxMana(), 10);
            }


    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + getName() + ":", 10);
        typewriter("1) Time Slash - 300 Base Damage - CD: " + getSkill1Cooldown(), 10);
        typewriter("2) Temporal Shift - Evade Next Attack - CD: " + getSkill2Cooldown(), 10);
        typewriter("3) Chrono Mark - Increase Damage by 25% - CD: " + getSkill3Cooldown(), 10);
        typewriter("0) Escape Battle", 10);

        boolean validChoice = false;
        while (!validChoice) {
            try {
                int choice;
                System.out.print("Enter the number of your choice: ");
                choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        if (getSkill1Cooldown() > 0) {
                            typewriter("Skill is on cooldown for " + getSkill1Cooldown() + " more turns!", 5);
                        } else {
                            skill1(target);
                            validChoice = true;
                        }
                    }
                    case 2 -> {
                        if (getSkill2Cooldown() > 0) {
                            typewriter("Skill is on cooldown for " + getSkill2Cooldown() + " more turns!", 5);
                        } else {
                            skill2(target);
                            validChoice = true;
                        }
                    }
                    case 3 -> {
                        if (getSkill3Cooldown() > 0) {
                            typewriter("Skill is on cooldown for " + getSkill3Cooldown() + " more turns!", 5);
                        } else {
                            skill3(target);
                        }
                    }
                    case 0 -> {
                        typewriter(getName() + " attempts to flee the battle!", 10);
                        setEscaped(true);
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

