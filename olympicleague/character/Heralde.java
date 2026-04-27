package character;
import java.util.*;

public class Heralde extends GameCharacter {
    Scanner scan = new Scanner(System.in);

    public Heralde(){
        super("Heralde", """
                            Legends say Heralde strangled a lion with his bare hands as a boy.
                            His rage against monsters earned Zeus' favor, but each victory leaves him hungrier for the next challenge.""",
                1800, 450,  "Skill 1  Lion's Strike (90 mana, 2-turn CD). Deals 220 damage single target.",
                            "Skill 2  Iron Hide (100 mana, 3-turn CD). Reduce incoming damage by 30% for 2 turns.",
                            "Skill 3 (God-Gift  Zeus): Thunder Wrath (180 mana, 6-turn CD). Zeus' lightning strikes, 400 true damage."
        );
    }

    @Override
    public void skill1(GameCharacter target){
        if(getSkill1Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " uses LION'S STRIKE!", 30);
        }
        if(getMana() >= 90){
            this.useMana(90);
            setSkill1Cooldown(1);

            int baseDamage = 220;
            int damage = randomDamage(baseDamage, 20); // ±20 damage variance

            typewriter("A Lion's strike lands on " + target.getName() + "!", 30);
            typewriter("Dealt " + damage + " damage to " + target.getName() + "!", 10);
            target.takeDamage(damage);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }
    @Override
    public void skill2(GameCharacter target){
        if(getSkill2Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " uses IRON HIDE!", 30);
        }
        if(getMana() >= 100){
            this.useMana(100);
            setSkill2Cooldown(3);

            setDefenseBonus(0.7); // Reduce incoming damage by 30%
            setStatusEffectTurns(2); // Lasts for 2 turns

            typewriter(getName() + "'s skin hardens, reducing incoming damage by 30% for 2 turns!", 10);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }
    @Override
    public void skill3(GameCharacter target){
        if(getSkill3Cooldown() > 0){
            return;
        }else{
            typewriter("\n" + getName() + " uses THUNDER WRATH!", 30);
        }
        if(getMana() >= 180){
            this.useMana(180);
            setSkill3Cooldown(6);

            int trueDamage = 400; // True damage ignores defenses

            typewriter("Zeus' lightning strikes " + target.getName() + " for " + trueDamage + " true damage!", 10);
            target.takeTrueDamage(trueDamage);
        }else{
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
        if(getSkill1Cooldown() > 0) {
            typewriter("Lion's Strike is on cooldown for " + getSkill1Cooldown() + " turns.", 10);
        }

        if(getSkill2Cooldown() > 0) {
            typewriter("Iron Hide is on cooldown for " + getSkill2Cooldown() + " turns.", 10);
        }

        if(getSkill3Cooldown() > 0) {
            typewriter("Thunder Wrath is on cooldown for " + getSkill3Cooldown() + " turns.", 10);
        }
        System.out.println();
        typewriter(getName() + " - Health: " + getHealth() + "/" + getMaxHealth() + " | Mana: " + getMana() + "/" + getMaxMana(), 10);
    }
    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + getName() + ":", 10);
        typewriter("1) Lion's Strike - 220 Base Damage - CD: " + getSkill1Cooldown(), 10);
        typewriter("2) Iron Hide - Reduce incoming damage by 30% for 2 turns - CD: " + getSkill2Cooldown(), 10);
        typewriter("3) Thunder Wrath (God-Gift Zeus) - 400 True Damage - CD: " + getSkill3Cooldown(), 10);
        typewriter("0) Escape Battle", 10);

        boolean validChoice = false;
        while (!validChoice) {
            try{
                int choice;
                System.out.print("Enter the number of your choice: ");
                choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        if(getSkill1Cooldown() > 0) {
                            typewriter("Skill is on cooldown for " + getSkill1Cooldown() + " more turns!", 5);
                        } else {
                            skill1(target);
                            validChoice = true;
                        }
                    }
                    case 2 -> {
                        if(getSkill2Cooldown() > 0) {
                            typewriter("Skill is on cooldown for " + getSkill2Cooldown() + " more turns!", 5);
                        } else {
                            skill2(target);
                            validChoice = true;
                        }
                    }
                    case 3 -> {
                        if(getSkill3Cooldown() > 0) {
                            typewriter("Skill is on cooldown for " + getSkill3Cooldown() + " more turns!", 5);
                            } else {
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