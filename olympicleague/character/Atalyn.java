package ligaolympica.character;
import java.util.*;

public class Atalyn extends GameCharacter {
    Scanner scan = new Scanner(System.in);
    private boolean nextAttackEvaded = false;


    public Atalyn() {
        super("Atalyn", """
                                              Hunter of Artemis - Raised in forests where mortals fear to tread, Atalyn's arrow never misses.
                                              Said to be blessed at birth by Artemis, she hunts not for food, but for perfection in the chase.""",
              1500, 950, "Skill 1: Piercing Arrow - A precise shot that deals 360 damage and ignores 20% of armor.", "Skill 2: Hunter's Reflex - Evade the next attack.", "Gods Gift: Moonlit Mark - Mark the target, increasing damage dealt by 50% for 2 turns.");
    }

    @Override
    public void skill1(GameCharacter target) {
        if(skill1Cooldown > 0){
            return;
        }else{
        typewriter("\n" + name + " fires a PIERCING ARROW!", 30);
        }
        if (this.mana >= 150) {
            this.useMana(150);
            this.skill1Cooldown = 1;

            int baseDamage = 360;
            int damage = randomDamage(baseDamage, 18); // ±18 damage variance

            typewriter("The arrow cuts through the air in a perfect line!", 30);
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
        typewriter("\n" + name + " activates HUNTER'S REFLEX!", 30);
        }
        if (this.mana >= 120) {
            this.useMana(120);
            this.skill2Cooldown = 2;

            this.nextAttackEvaded = true;
            typewriter("Atalyn's reflexes sharpen - the next attack will be evaded!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(skill3Cooldown > 0){
            return;
        }else{
        typewriter("\n" + name + " casts MOONLIT MARK!", 30);
        }
        if (this.mana >= 500) {
            this.useMana(500);
            this.skill3Cooldown = 5;

            this.statusEffectTurns = 2;
            this.attackBonus = 1.5;

            typewriter("ARTEMIS' BLESSING! Moonlight marks the target - damage increased by 50% for 2 turns!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }
    @Override
    public void displayStats() {
            if(skill1Cooldown > 0) {
                typewriter("Piercing Arrow is on cooldown for " + this.skill1Cooldown + " turns.", 10);
            }
            if(skill2Cooldown > 0) {
                typewriter("Hunter's Reflex is on cooldown for " + this.skill2Cooldown + " turns.", 10);
            }
            if(skill3Cooldown > 0) {
                typewriter("Moonlit Mark is on cooldown for " + this.skill3Cooldown + " turns.", 10);
            }

            System.out.println();
            typewriter(name + " - Health: " + health + "|" + maxHealth + ", Mana: " + mana + "/" + maxMana, 10);
            }

    @Override
    public void takeDamage(int damage) {
        if (nextAttackEvaded) {
            typewriter(this.name + " gracefully evades the attack!", 30);
            nextAttackEvaded = false;
            return;
        }
        super.takeDamage(damage);
    }

    @Override
    public void restoreMana(int amount) {
        this.mana = Math.min(this.mana + amount, this.maxMana);
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + name + ":", 10);
        typewriter("1) Piercing Arrow - 360 Base Damage - CD: " + skill1Cooldown, 10);
        typewriter("2) Hunter's Reflex - Evade Next Attack - CD: " + skill2Cooldown, 10);
        typewriter("3) Moonlit Mark - Increase Damage by 50% - CD: " + skill3Cooldown, 10);
        typewriter("0) Escape Battle", 10);

        boolean validChoice = false;
            while (!validChoice) {
            try{
                int choice;
                System.out.print("Enter the number of your choice: ");
                choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        if(skill1Cooldown > 0) {
                            typewriter("Skill is on cooldown for " + skill1Cooldown + " more turns!", 5);
                        } else {
                            skill1(target);
                            validChoice = true;
                        }
                    }
                    case 2 -> {
                        if(skill2Cooldown > 0) {
                            typewriter("Skill is on cooldown for " + skill2Cooldown + " more turns!", 5);

                        } else {
                            skill2(target);
                            validChoice = true;
                        }
                    }
                    case 3 -> {
                        if(skill3Cooldown > 0) {
                            typewriter("Skill is on cooldown for " + skill3Cooldown + " more turns!", 5);
                        } else {
                            skill3(target);
                            validChoice = true;
                            }
                    }
                    case 0 -> {
                        typewriter(name + " attempts to flee the battle!", 10);
                        this.hasEscaped = true;
                        validChoice = true;
                        return;
                    }
                    default -> {
                        typewriter("Invalid choice.", 5);
                        scan.nextLine();
                    }
                }
            }catch(Exception e){
                typewriter("Invalid input. Please enter a number between 1 and 3.", 5);
                scan.nextLine(); // clear invalid input
            }
        }
    }
}

