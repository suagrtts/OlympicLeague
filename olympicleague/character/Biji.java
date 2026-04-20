package ligaolympica.character;
import java.util.*;

public class Biji extends GameCharacter {
    Scanner scan = new Scanner(System.in);

    public Biji() {
        super("Biji", """
                        The Melodic Warrior - Blessed by Apollo, god of music and arts, Biji wields a divine guitar that can shatter enemies with sonic waves.
                        His melodies can heal allies or unleash devastating harmonics that tear through armor and soul alike.""",
                1600, 1200,
                "Skill 1: Power Chord - Strikes a devastating guitar chord that sends sonic waves crashing into enemies. Deals 380 damage.",
                "Skill 2: Healing Hymn - Plays a soothing melody blessed by Apollo. Restores 400 HP.",
                "Gods Gift: Symphony of Destruction - Channels Apollo's divine music. Deals massive damage to opponent and stuns them.");
    }

    @Override
    public void skill1(GameCharacter target) {
        if(skill1Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " shreds a POWER CHORD!", 30);
        }
        if (this.mana >= 200) {
            this.useMana(200);
            this.skill1Cooldown = 1;

            int baseDamage = 380;
            int damage = randomDamage(baseDamage, 25);

            typewriter("Sonic waves crash into " + target.getName() + "!", 30);
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
            typewriter("\n" + name + " plays a HEALING HYMN!", 30);
        }
        if (this.mana >= 300) {
            this.useMana(300);
            this.skill2Cooldown = 3;

            int healAmount = 400;
            this.heal(healAmount);
            typewriter("Apollo's blessing flows through the melody!", 30);
            typewriter(name + " recovered " + healAmount + " HP!", 10);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(skill3Cooldown > 0){
            return;
        }else{
            typewriter("\n" + name + " unleashes SYMPHONY OF DESTRUCTION!", 10);
        }
        if (this.mana >= 550) {
            this.useMana(550);
            this.skill3Cooldown = 5;

            int baseDamage = 600;
            int damage = randomDamage(baseDamage, 30);

            typewriter("Divine music overwhelms the battlefield!", 30);
            typewriter("Dealt " + damage + " damage! to " + target.getName() + " and is stunned ", 10);
            target.takeDamage(damage);
            target.isStunned = true;
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
        if(skill1Cooldown > 0) {
            typewriter("Power Chord is on cooldown for " + this.skill1Cooldown + " turns.", 10);
        }
        if(skill2Cooldown > 0) {
            typewriter("Healing Hymn is on cooldown for " + this.skill2Cooldown + " turns.", 10);
        }
        if(skill3Cooldown > 0) {
            typewriter("Symphony of Destruction is on cooldown for " + this.skill3Cooldown + " turns.", 10);
        }
        System.out.println();
        typewriter(name + " - Health: " + health + "|" + maxHealth + " Mana: " + mana + "/" + maxMana, 10);
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + name + ":", 30);
        typewriter("1) Power Chord - 380 Base Damage - CD: " + skill1Cooldown, 30);
        typewriter("2) Healing Hymn - Heal 400 HP - CD: " + skill2Cooldown, 30);
        typewriter("3) Symphony of Destruction - 600 Damage and stuns them - CD: " + skill3Cooldown, 30);
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
                scan.nextLine();
            }
        }
    }
}