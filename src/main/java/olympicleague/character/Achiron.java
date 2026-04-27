package character;

import java.util.Scanner;

public class Achiron extends GameCharacter {

    private Scanner scan = new Scanner(System.in);

    public Achiron() {
        super(
            "Achiron",
            """
            The Unyielding Warrior - A descendant of Achilles, Achiron was trained by old warlords who believed pain forged immortality.
            Scarred in a hundred duels, he refuses to bow, claiming even the gods cannot break his will.
            """,
            1800,
            1000,
            "Skill 1: Spear Thrust - A powerful thrust with a spear. Deals 400 damage and pierces armor.",
            "Skill 2: Aegis Shield - Achiron raises his shield to block incoming attack. Reduces damage by 50%.",
            "Gods Gift: Wrath of Ares - Unleash the fury of the god of war. Increases damage by 50% for 2 turns."
        );
    }

    @Override
    public void skill1(GameCharacter target) {
        if (getSkill1Cooldown() > 0) return;

        typewriter("\n" + getName() + " uses SPEAR THRUST!", 30);

        if (getMana() >= 180) {
            useMana(180);
            setSkill1Cooldown(1);

            int baseDamage = 400;
            int damage = randomDamage(baseDamage, 20);

            typewriter("Armor piercing spear thrust!", 30);
            typewriter("Dealt " + damage + " damage to " + target.getName() + "!", 10);

            target.takeDamage(damage);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill2(GameCharacter target) {
        if (getSkill2Cooldown() > 0) return;

        typewriter("\n" + getName() + " uses AEGIS SHIELD!", 30);

        if (getMana() >= 320) {
            useMana(320);
            setSkill2Cooldown(3);

            // Reduce incoming damage by 50% for the next attack
            setDefenseBonus(0.5);
            setStatusEffectTurns(1);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if (getSkill3Cooldown() > 0) return;

        typewriter("\n" + getName() + " channels WRATH OF ARES!", 10);

        if (getMana() >= 500) {
            useMana(500);
            setSkill3Cooldown(5);

            // +50% damage for 2 turns
            setAttackBonus(1.5);
            setStatusEffectTurns(2);

            typewriter("WRATH OF ARES ACTIVATED! +50% damage for 2 turns!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (getStatusEffectTurns() > 0 && getDefenseBonus() == 0.5) {
            damage = (int) (damage * 0.5);
            typewriter(getName() + " blocks with Aegis Shield! Damage reduced to " + damage + "!", 10);

            setDefenseBonus(1.0);
            setStatusEffectTurns(0);
        }

        super.takeDamage(damage);
    }

    @Override
    public void displayStats() {
        if (getSkill1Cooldown() > 0) {
            typewriter("Spear Thrust is on cooldown for " + getSkill1Cooldown() + " turns.", 10);
        }
        if (getSkill2Cooldown() > 0) {
            typewriter("Aegis Shield is on cooldown for " + getSkill2Cooldown() + " turns.", 10);
        }
        if (getSkill3Cooldown() > 0) {
            typewriter("Wrath of Ares is on cooldown for " + getSkill3Cooldown() + " turns.", 10);
        }

        System.out.println();
        typewriter(
            getName() + " - Health: " + getHealth() + "|" + getMaxHealth() +
            " Mana: " + getMana() + "/" + getMaxMana(),
            10
        );
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + getName() + ":", 30);
        typewriter("1) Spear Thrust - 400 Base Damage - CD: " + getSkill1Cooldown(), 30);
        typewriter("2) Aegis Shield - Reduce Damage by 50% - CD: " + getSkill2Cooldown(), 30);
        typewriter("3) Wrath of Ares - Increase Damage by 50% - CD: " + getSkill3Cooldown(), 30);
        typewriter("0) Escape Battle", 30);

        boolean validChoice = false;

        while (!validChoice) {
            try {
                System.out.print("Enter the number of your choice: ");
                int choice = scan.nextInt();

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
                            validChoice = true;
                        }
                    }
                    case 0 -> {
                        typewriter(getName() + " attempts to flee the battle!", 10);
                        setEscaped(true);
                        return;
                    }
                    default -> {
                        typewriter("Invalid choice.", 10);
                        scan.nextLine();
                    }
                }
            } catch (Exception e) {
                typewriter("Invalid input. Please enter a number between 1 and 3.", 5);
                scan.nextLine();
            }
        }
    }
}