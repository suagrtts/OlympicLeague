package character;

import java.util.Scanner;

public class Atalyn extends GameCharacter {

    private Scanner scan = new Scanner(System.in);
    private boolean nextAttackEvaded = false;

    public Atalyn() {
        super(
            "Atalyn",
            """
            Hunter of Artemis - Raised in forests where mortals fear to tread, Atalyn's arrow never misses.
            Said to be blessed at birth by Artemis, she hunts not for food, but for perfection in the chase.
            """,
            1500,
            950,
            "Skill 1: Piercing Arrow - A precise shot that deals 360 damage and ignores 20% of armor.",
            "Skill 2: Hunter's Reflex - Evade the next attack.",
            "Gods Gift: Moonlit Mark - Mark the target, increasing damage dealt by 50% for 2 turns."
        );
    }

    @Override
    public void skill1(GameCharacter target) {
        if (getSkill1Cooldown() > 0) return;

        typewriter("\n" + getName() + " fires a PIERCING ARROW!", 30);

        if (getMana() >= 150) {
            useMana(150);
            setSkill1Cooldown(1);

            int baseDamage = 360;
            int damage = randomDamage(baseDamage, 18);

            typewriter("The arrow cuts through the air in a perfect line!", 30);
            typewriter("Dealt " + damage + " damage to " + target.getName() + "!", 10);

            target.takeDamage(damage);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill2(GameCharacter target) {
        if (getSkill2Cooldown() > 0) return;

        typewriter("\n" + getName() + " activates HUNTER'S REFLEX!", 30);

        if (getMana() >= 120) {
            useMana(120);
            setSkill2Cooldown(2);

            nextAttackEvaded = true;
            typewriter("Atalyn's reflexes sharpen - the next attack will be evaded!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void skill3(GameCharacter target) {
        if (getSkill3Cooldown() > 0) return;

        typewriter("\n" + getName() + " casts MOONLIT MARK!", 30);

        if (getMana() >= 500) {
            useMana(500);
            setSkill3Cooldown(5);

            setStatusEffectTurns(2);
            setAttackBonus(1.5);

            typewriter("ARTEMIS' BLESSING! Moonlight marks the target - damage increased by 50% for 2 turns!", 30);
        } else {
            typewriter("Not enough mana!", 30);
        }
    }

    @Override
    public void displayStats() {
        if (getSkill1Cooldown() > 0) {
            typewriter("Piercing Arrow is on cooldown for " + getSkill1Cooldown() + " turns.", 10);
        }
        if (getSkill2Cooldown() > 0) {
            typewriter("Hunter's Reflex is on cooldown for " + getSkill2Cooldown() + " turns.", 10);
        }
        if (getSkill3Cooldown() > 0) {
            typewriter("Moonlit Mark is on cooldown for " + getSkill3Cooldown() + " turns.", 10);
        }

        System.out.println();
        typewriter(getName() + " - Health: " + getHealth() + "/" + getMaxHealth() +
                ", Mana: " + getMana() + "/" + getMaxMana(), 10);
    }

    @Override
    public void takeDamage(int damage) {
        if (nextAttackEvaded) {
            typewriter(getName() + " gracefully evades the attack!", 30);
            nextAttackEvaded = false;
            return;
        }
        super.takeDamage(damage);
    }

    @Override
    public void restoreMana(int amount) {
        setMana(Math.min(getMana() + amount, getMaxMana()));
    }

    @Override
    public void takeTurn(GameCharacter target) {
        typewriter("\nChoose a skill for " + getName() + ":", 10);
        typewriter("1) Piercing Arrow - 360 Base Damage - CD: " + getSkill1Cooldown(), 10);
        typewriter("2) Hunter's Reflex - Evade Next Attack - CD: " + getSkill2Cooldown(), 10);
        typewriter("3) Moonlit Mark - Increase Damage by 50% - CD: " + getSkill3Cooldown(), 10);
        typewriter("0) Escape Battle", 10);

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
                        typewriter("Invalid choice.", 5);
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