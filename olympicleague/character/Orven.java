package ligaolympica.character;
import java.util.*;

public class Orven extends GameCharacter {
    Scanner scan = new Scanner(System.in);

    public Orven() {
        super("Orven", """
                            A demigod son of Hermes, Orven is swift and cunning.
                            His agility in battle is unmatched, and his quick wit often turns the tide of conflict.""",
                1600, 700,  "Skill 1: Swift Strike - A rapid attack that deals 250 damage.",
                            "Skill 2: Vanish - Become untargetable for the next turn.",
                            "Gods Gift: Hermes' Speed - Move so fast that you can attack twice in one turn."
        );
    }

    @Override
    public void skill1(GameCharacter target){
        if(skill1Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " uses SWIFT STRIKE!", 30);
        }
        if(this.mana >= 140){
            this.useMana(140);
            this.skill1Cooldown = 1;

            int baseDamage = 250;
            int damage = randomDamage(baseDamage, 20); // ±20 damage variance

            typewriter("A swift strike hits " + target.getName() + "!", 30);
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
            typewriter("\n" + name + " uses VANISH!", 30);
        }
        if(this.mana >= 120){
            this.useMana(120);
            this.skill2Cooldown = 3;

            this.untargetable = true; // Become untargetable
            this.statusEffectTurns = 1; // Lasts for next turn

            typewriter(name + " vanishes from sight, becoming untargetable for the next turn!", 10);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }
    @Override
    public void skill3(GameCharacter target){
        if(skill3Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " activates HERMES' SPEED!", 30);
        }
        if(this.mana >= 200){
            this.useMana(200);
            this.skill3Cooldown = 5;

            //Just attack twice immediately instead of setting a flag
            typewriter(name + " moves with the speed of Hermes!", 10);
            typewriter("\n>>> First Strike! <<<", 20);
            skill1(target);

            if(target.isAlive()) {
                this.skill1Cooldown = 0; // Reset cooldown to allow second attack
                typewriter("\n>>> Second Strike! <<<", 20);
                skill1(target);
            }
        }else{
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if(this.statusEffectTurns > 0 && this.untargetable){
            typewriter(name + " evades the attack using VANISH!", 30);
            return; // No damage taken
        }
        super.takeDamage(damage);
    }
    @Override
    public void displayStats() {
        if(skill1Cooldown > 0){
            typewriter("Swift Strike is on cooldown for " + skill1Cooldown + " turns.\n", 10);
        }
        if(skill2Cooldown > 0){
            typewriter("Vanish is on cooldown for " + skill2Cooldown + " turns.\n", 10);
        }
        if(skill3Cooldown > 0){
            typewriter("Hermes' Speed is on cooldown for " + skill3Cooldown + " turns.\n", 10);
        }
        System.out.println();
        typewriter(name + " - Health: " + health + "|" + maxHealth + ", Mana: " + mana + "/" + maxMana, 10);
    }

    @Override
    public void takeTurn(GameCharacter target){
        typewriter("\nChoose a skill for " + name + ":", 10);
        typewriter("1) Swift Strike - 250 Base Damage - CD: " + skill1Cooldown, 10);
        typewriter("2) Vanish - Untargetable Next Turn - CD: " + skill2Cooldown, 10);
        typewriter("3) Hermes' Speed - Extra Attack This Turn - CD: " + skill3Cooldown, 10);
        typewriter("0) Escape Battle", 10);

        boolean validChoice = false;
        while(!validChoice) {
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
                    scan.nextLine();
            }
        }
    }
}
