package character;
import java.util.*;

public class Selwyn extends GameCharacter {
    Scanner scan = new Scanner(System.in);

    public Selwyn() {
        super("Selwyn", """
                        The Digital Overlord - Blessed by Loki, god of mischief and trickery, Selwyn is a legendary gamer who mastered every realm.
                        With lightning reflexes and strategic genius, he exploits enemy weaknesses like finding glitches in reality itself.""",
                1700, 1100,
                "Skill 1: Rage Bait - Unleashes gamer rage dealing massive damage. 420 base damage.",
                "Skill 2: Respawn Shield - Creates a defensive barrier. Reduces next incoming damage by 60%.",
                "Gods Gift: Loki's Hack - Exploits reality's code. Critical strike that ignores 30% of enemy defenses for 450 damage.");
    }

    @Override
    public void skill1(GameCharacter target) {
        if(getSkill1Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " triggers a RAGE BAIT!", 30);
        }
        if (getMana() >= 220) {
            this.useMana(220);
            setSkill1Cooldown(1);

            int baseDamage = 420;
            int damage = randomDamage(baseDamage, 30);

            typewriter("Keyboard smashing intensifies!", 30);
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
            typewriter("\n" + getName() + " activates RESPAWN SHIELD!", 30);
        }
        if (getMana() >= 280) {
            this.useMana(280);
            setSkill2Cooldown(3);

            setDefenseBonus(0.4); // Takes only 40% damage
            setStatusEffectTurns(1);
            typewriter("Respawn Shield activated! Next attack damage reduced by 60%!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if(getSkill3Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " executes LOKI'S HACK!", 10);
        }
        if (getMana() >= 480) {
            this.useMana(480);
            setSkill3Cooldown(5);

            int baseDamage = 450;
            int damage = randomDamage(baseDamage, 25);

            typewriter("Exploiting reality's vulnerabilities!", 30);
            typewriter("Dealt " + damage + " damage!", 10);
            target.takeDamage(damage);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (getStatusEffectTurns() > 0 && getDefenseBonus() == 0.4) {
            damage = (int)(damage * 0.4);
            typewriter(getName() + " blocks with Respawn Shield! Damage reduced to " + damage + "!", 10);
            setDefenseBonus(1.0);
            setStatusEffectTurns(0);
        }
        super.takeDamage(damage);
    }

    @Override
    public void displayStats() {
        if(getSkill1Cooldown() > 0) {
            typewriter("Rage Bait is on cooldown for " + getSkill1Cooldown() + " turns.", 10);
        }
        if(getSkill2Cooldown() > 0) {
            typewriter("Respawn Shield is on cooldown for " + getSkill2Cooldown() + " turns.", 10);
        }
        if(getSkill3Cooldown() > 0) {
            typewriter("Loki's Hack is on cooldown for " + getSkill3Cooldown() + " turns.", 10);
        }
        System.out.println();
        typewriter(getName() + " - Health: " + getHealth() + "|" + getMaxHealth() + " Mana: " + getMana() + "/" + getMaxMana(), 10);
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + getName() + ":", 30);
        typewriter("1) Rage Bait - 420 Base Damage - CD: " + getSkill1Cooldown(), 30);
        typewriter("2) Respawn Shield - Reduce Next Damage by 60% - CD: " + getSkill2Cooldown(), 30);
        typewriter("3) Loki's Hack - 450 Critical Damage (Ignores Defense) - CD: " + getSkill3Cooldown(), 30);
        typewriter("0) Escape Battle", 10);

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