package ligaolympica.character;
import java.util.*;

public class Achiron extends GameCharacter {
    Scanner scan = new Scanner(System.in);
    public Achiron() {
        super("Achiron", """
                                                  The Unyielding Warrior - A descendant of Achilles, Achiron was trained by old warlords who believed pain forged immortality.
                                                  Scarred in a hundred duels, he refuses to bow, claiming even the gods cannot break his will.""",
              1800, 1000, "Skill 1: Spear Thrust - A powerful thrust with a spear. Deals 400 damage and pierces armor.", "Skill 2: Aegis Shield - Achiron raises his shield to block incoming attack. Reduces damage by 50%.", "Gods Gift: Wrath of Ares - Unleash the fury of the god of war. Increases damage by 50% for 2 turns.");
    }

    @Override
    public void skill1(GameCharacter target) {
        if(skill1Cooldown > 0){
            return;
        }else{
        typewriter("\n" + name + " uses SPEAR THRUST!", 30);
        }
        if (this.mana >= 180) {
            this.useMana(180);
            this.skill1Cooldown = 1;

            int baseDamage = 400;
            int damage = randomDamage(baseDamage, 20); // ±20 damage variance

            typewriter("Armor piercing spear thrust!", 30);
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
        typewriter("\n" + name + " uses AEGIS SHIELD!", 30);
        }
        if (this.mana >= 320) {
            this.useMana(320);
            this.skill2Cooldown = 3;

            // Reduce incoming damage by 50% for the next attack
            this.defenseBonus = 0.5;

            this.statusEffectTurns = 1;
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(skill3Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " channels WRATH OF ARES!", 10);
        }
        if (this.mana >= 500) {
            this.useMana(500);
            this.skill3Cooldown = 5;

            // +50% damage for 2 turns
            this.attackBonus = 1.5;
            this.statusEffectTurns = 2;
            typewriter("WRATH OF ARES ACTIVATED! +50% damage for 2 turns!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (this.statusEffectTurns > 0 && this.defenseBonus == 0.5) {
            damage = (int)(damage * 0.5);
            typewriter(name + " blocks with Aegis Shield! Damage reduced to " + damage + "!", 10);
            this.defenseBonus = 1.0; // Reset damage bonus after blocking
            this.statusEffectTurns = 0; // Shield bash effect used up
        }
        super.takeDamage(damage);
    }

    @Override
    public void displayStats() {
            if(skill1Cooldown > 0) {
                typewriter("Spear Thrust is on cooldown for " + this.skill1Cooldown + " turns.", 10);
            }
            if(skill2Cooldown > 0) {
                typewriter("Aegis Shield is on cooldown for " + this.skill2Cooldown + " turns.", 10);
            }
            if(skill3Cooldown > 0) {
                typewriter("Wrath of Ares is on cooldown for " + this.skill3Cooldown + " turns.", 10);
            }
            System.out.println();
            typewriter(name + " - Health: " + health + "|" + maxHealth + " Mana: " + mana + "/" + maxMana, 10);
            }

    @Override
    public void takeTurn(GameCharacter target) {
            typewriter("\nChoose a skill for " + name + ":", 30);
            typewriter("1) Spear Thrust - 400 Base Damage - CD: " + skill1Cooldown, 30);
            typewriter("2) Aegis Shield - Reduce Damage by 50% - CD: " + skill2Cooldown, 30);
            typewriter("3) Wrath of Ares - Increase Damage by 50% - CD: " + skill3Cooldown, 30);
            typewriter("0) Escape Battle", 30);

            boolean validChoice = false;
            while (!validChoice) {
                try{
                    int choice;
                    System.out.print("Enter the number of your choice: ");
                    choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        if(skill1Cooldown > 0){
                            typewriter("Skill is on cooldown for " + skill1Cooldown + " more turns!", 5);
                        }else {
                            skill1(target);
                            validChoice = true;
                        }
                    }
                    case 2 -> {
                        if(skill2Cooldown > 0){
                            typewriter("Skill is on cooldown for " + skill2Cooldown + " more turns!", 5);
                        }else {
                            skill2(target);
                            validChoice = true;
                        }
                    }
                    case 3 -> {
                        if(skill3Cooldown > 0){
                            typewriter("Skill is on cooldown for " + skill3Cooldown + " more turns!", 5);
                        }else {
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
                    typewriter("Invalid choice.", 10);
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

