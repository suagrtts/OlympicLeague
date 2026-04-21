package olympicleague.character;
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
        if(getSkill1Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " shreds a POWER CHORD!", 30);
        }
        if (getMana() >= 200) {
            this.useMana(200);
            setSkill1Cooldown(1);

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
        if(getSkill2Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " plays a HEALING HYMN!", 30);
        }
        if (getMana() >= 300) {
            this.useMana(300);
            setSkill2Cooldown(3);

            int healAmount = 400;
            this.heal(healAmount);
            typewriter("Apollo's blessing flows through the melody!", 30);
            typewriter(getName() + " recovered " + healAmount + " HP!", 10);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(getSkill3Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " unleashes SYMPHONY OF DESTRUCTION!", 10);
        }
        if (getMana() >= 550) {
            this.useMana(550);
            setSkill3Cooldown(5);

            int baseDamage = 600;
            int damage = randomDamage(baseDamage, 30);

            typewriter("Divine music overwhelms the battlefield!", 30);
            typewriter("Dealt " + damage + " damage! to " + target.getName() + " and is stunned ", 10);
            target.takeDamage(damage);
            target.setStunned(true);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
        if(getSkill1Cooldown() > 0) {
            typewriter("Power Chord is on cooldown for " + getSkill1Cooldown() + " turns.", 10);
        }
        if(getSkill2Cooldown() > 0) {
            typewriter("Healing Hymn is on cooldown for " + getSkill2Cooldown() + " turns.", 10);
        }
        if(getSkill3Cooldown() > 0) {
            typewriter("Symphony of Destruction is on cooldown for " + getSkill3Cooldown() + " turns.", 10);
        }
        System.out.println();
        typewriter(getName() + " - Health: " + getHealth() + "|" + getMaxHealth() + " Mana: " + getMana() + "/" + getMaxMana(), 10);
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + getName() + ":", 30);
        typewriter("1) Power Chord - 380 Base Damage - CD: " + getSkill1Cooldown(), 30);
        typewriter("2) Healing Hymn - Heal 400 HP - CD: " + getSkill2Cooldown(), 30);
        typewriter("3) Symphony of Destruction - 600 Damage and stuns them - CD: " + getSkill3Cooldown(), 30);
        typewriter("0) Escape Battle", 30);

        boolean validChoice = false;
        while (!validChoice) {
            try{
                int choice;
                System.out.print("Enter the number of your choice: ");
                choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        if(getSkill1Cooldown() > 0){
                            typewriter("Skill is on cooldown for " + getSkill1Cooldown() + " more turns!", 5);
                        }else {
                            skill1(target);
                            validChoice = true;
                        }
                    }
                    case 2 -> {
                        if(getSkill2Cooldown() > 0){
                            typewriter("Skill is on cooldown for " + getSkill2Cooldown() + " more turns!", 5);
                        }else {
                            skill2(target);
                            validChoice = true;
                        }
                    }
                    case 3 -> {
                        if(getSkill3Cooldown() > 0){
                            typewriter("Skill is on cooldown for " + getSkill3Cooldown() + " more turns!", 5);
                        }else {
                            skill3(target);
                            validChoice = true;
                        }
                    }
                    case 0 -> {
                        typewriter(getName() + " attempts to flee the battle!", 10);
                        setEscaped(true);
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