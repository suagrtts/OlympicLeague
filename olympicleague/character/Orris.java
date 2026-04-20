package ligaolympica.character;
import java.util.*;

public class Orris extends GameCharacter {
    Scanner scan = new Scanner(System.in);

    public Orris() {
        super("Orris", """
                            A demigod son of Poseidon, Orris commands the power of water and storms.
                            His temper is as fierce as the sea, and his loyalty to friends is unwavering.""",
                1500, 600,  "Skill 1: Tidal Wave - Unleash a wave that deals 300 damage to all enemies.",
                            "Skill 2: Ocean's Shield - Create a shield that absorbs 20% damage for 2 turns.",
                            "Gods Gift: Poseidon's Wrath - Summon a devastating tsunami that deals 500 true damage to all enemies."
        );
    }

    @Override
    public void skill1(GameCharacter target){
        if(skill1Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " summons a TIDAL WAVE!", 30);
        }
        if(this.mana >= 130){
            this.useMana(130);
            this.skill1Cooldown = 1;

            int baseDamage = 300;
            int damage = randomDamage(baseDamage, 25); // ±25 damage variance

            typewriter("A Tidal wave crashes onto " + target.getName() + "!", 30);
            typewriter("Dealt " + damage + " damage to " + target.getName() + "!", 10);
            target.takeDamage(damage);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }
    @Override
    public void skill2(GameCharacter target){
        if(skill2Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " activates OCEAN'S SHIELD!", 30);
        }
        if(this.mana >= 110){
            this.useMana(110);
            this.skill2Cooldown = 3;

            this.defenseBonus = 0.8; // Absorb 20% of incoming damage
            this.statusEffectTurns = 2; // Lasts for 2 turns

            typewriter(name + "'s shield rises, absorbing incoming damage for 2 turns!", 10);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }
    @Override
    public void skill3(GameCharacter target){
        if(skill3Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " calls upon POSEIDON'S WRATH!", 30);
        }
        if(this.mana >= 180){
            this.useMana(180);
            this.skill3Cooldown = 6;

            int trueDamage = 500; // True damage ignores defenses

            typewriter("A Poseidon's wrath crashes down on " + target.getName() + "!", 30);
            typewriter("Dealt " + trueDamage + " true damage to " + target.getName() + "!", 10);
            target.takeTrueDamage(trueDamage);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
            if(skill1Cooldown > 0) {
                typewriter("Tidal Wave is on cooldown for " + this.skill1Cooldown + " turns.", 10);
            }
            if(skill2Cooldown > 0) {
                typewriter("Ocean's Shield is on cooldown for " + this.skill2Cooldown + " turns.", 10);
            }
            if(skill3Cooldown > 0) {
                typewriter("Poseidon's Wrath is on cooldown for " + this.skill3Cooldown + " turns.", 10);
            }

            System.out.println();
            typewriter(name + " - Health: " + health + "|" + maxHealth + ", Mana: " + mana + "/" + maxMana, 10);
            }
    @Override
    public void takeDamage(int damage) {
        // If Ocean's Shield is active, defenseBonus will be < 1.0 and statusEffectTurns > 0
        if (this.statusEffectTurns > 0 && this.defenseBonus < 1.0) {
            damage = (int)(damage * this.defenseBonus);
            typewriter(name + "'s Ocean's Shield absorbs part of the damage, reduced to " + damage + "!", 10);
        }
        super.takeDamage(damage);
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + name + ":", 10);
        typewriter("1) Tidal Wave - 300 Base Damage - CD: " + skill1Cooldown, 10);
        typewriter("2) Ocean's Shield - Absorb part of incoming damage for 2 turns - CD: " + skill2Cooldown, 10);
        typewriter("3) Poseidon's Wrath - 500 True Damage - CD: " + skill3Cooldown, 10);
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