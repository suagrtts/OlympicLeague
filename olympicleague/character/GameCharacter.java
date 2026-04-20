package ligaolympica.character;

import java.util.Random;

public class GameCharacter implements CharacterInterface {
    protected String name;
    protected String backstory;
    protected int health;
    protected int maxHealth;
    protected int mana;
    protected int maxMana;
    protected boolean isAlive;
    protected Random random;
    protected String skill1;
    protected String skill2;
    protected String skill3;

    // Status effects
    protected double attackBonus = 1.0;
    protected double defenseBonus = 1.0;
    protected int statusEffectTurns = 0;

    // Generic flags used by various characters
    protected boolean untargetable = false;
    protected boolean isStunned = false;
    protected boolean hasEscaped = false;

    // Cooldown tracking
    protected int skill1Cooldown = 0;
    protected int skill2Cooldown = 0;
    protected int skill3Cooldown = 0;

    public GameCharacter(String name, String backstory, int health, int mana, String skill1, String skill2, String skill3) {
        this.name = name;
        this.backstory = backstory;
        this.health = health;
        this.maxHealth = health;
        this.mana = mana;
        this.maxMana = mana;
        this.isAlive = true;
        this.random = new Random();
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
    }

    // Getters
    @Override
    public String getName() {
        return name;
    }
    public String getBackstory() {
        return backstory;
    }
    @Override
    public int getHealth() {
        return health;
    }
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
    @Override
    public int getMana() {
        return mana;
    }
    @Override
    public int getMaxMana() {
        return maxMana;
    }
    @Override
    public boolean isAlive() {
        return isAlive;
    }
    @Override
    public boolean isStunned() {
        return isStunned;
    }
    @Override
    public void setStunned(boolean stunned) {
        this.isStunned = stunned;
    }

    @Override
    public boolean hasEscaped() {
        return hasEscaped;
    }
    @Override
    public void setEscaped(boolean escaped) {
        this.hasEscaped = escaped;
    }

    @Override
    public  void skill1(GameCharacter target) {
    }
    @Override
    public  void skill2(GameCharacter target) {
    }
    @Override
    public  void skill3(GameCharacter target) {
    }

    //method to generate random damage within a range
    protected int randomDamage(int baseDamage, int variance) {
        int minDamage = Math.max(1, baseDamage - variance);
        int maxDamage = baseDamage + variance;
        int damage = random.nextInt(maxDamage - minDamage + 1) + minDamage;
        damage = (int)(damage * (0.85 + (0.3 * random.nextDouble())));
        damage = (int)(damage * attackBonus);
        return damage;
    }

    // Update status effects and cooldowns each turn
    @Override
    public void updateTurnEffects() {
        // Reduce cooldowns
        if (skill1Cooldown > 0) skill1Cooldown--;
        if (skill2Cooldown > 0) skill2Cooldown--;
        if (skill3Cooldown > 0) skill3Cooldown--;

        // Reduce status effect duration and reset temporary modifiers when they expire
        if (statusEffectTurns > 0) {
            statusEffectTurns--;
            if (statusEffectTurns == 0) {
                attackBonus = 1.0;
                defenseBonus = 1.0;
                untargetable = false;
            }
        }
    }

    @Override
    public void resetForNewRound() {
        this.health = this.maxHealth;
        this.mana = this.maxMana;
        this.isAlive = true;
        this.attackBonus = 1.0;
        this.defenseBonus = 1.0;
        this.statusEffectTurns = 0;
        this.untargetable = false;
        this.isStunned = false;
        this.hasEscaped = false;
        this.skill1Cooldown = 0;
        this.skill2Cooldown = 0;
        this.skill3Cooldown = 0;
    }

    @Override
    public void takeDamage(int damage) {
        if (!this.isAlive) return;

        // If untargetable, ignore damage
        if (this.untargetable || this.attackBonus == 0.0) {
            typewriter(this.name + " evades the attack!", 5);
            return;
        }

        //Apply damage modifier if a status effect is active
        if (this.statusEffectTurns > 0 && this.defenseBonus != 1.0) {
            damage = (int)(damage * this.defenseBonus);

            //Add feedback for damage reduction
            if (this.defenseBonus < 1.0) {
                typewriter(this.name + " reduced the damage to " + damage, 10);
            }
        }

        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
        }
    }

    // True damage ignores mitigation and untargetable
    @Override
    public void takeTrueDamage(int damage) {
        if (!this.isAlive) return;
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
        }
    }

    @Override
    public void heal(int amount) {
        this.health = Math.min(this.health + amount, this.maxHealth);
        System.out.println(this.name + " heals for " + amount + " HP! Health: " + this.health + "/" + this.maxHealth);
    }

    @Override
    public void useMana(int amount) {
        this.mana = Math.max(0, this.mana - amount);
    }

    @Override
    public void restoreMana(int amount) {
        this.mana = Math.min(this.mana + amount, this.maxMana);
    }

    @Override
    public void displayStats() {
        typewriter("\n" + name + " - HP: " + health + "/" + maxHealth + " | MP: " + mana + "/" + maxMana, 5);
        if (skill1Cooldown > 0 || skill2Cooldown > 0 || skill3Cooldown > 0) {
            typewriter("Cooldowns: ", 5);
            if (skill1Cooldown > 0) typewriter("Skill1(" + skill1Cooldown + ") ", 5);
            if (skill2Cooldown > 0) typewriter("Skill2(" + skill2Cooldown + ") ", 5);
            if (skill3Cooldown > 0) typewriter("Skill3(" + skill3Cooldown + ") ", 5);
            System.out.println();
        }
    }

    @Override
    public void showInfo() {
        typewriter("Character: " + name, 5);
        typewriter("Backstory: " + backstory, 5);
        typewriter("Health: " + maxHealth, 5);
        typewriter("Mana: " + maxMana, 5);
        typewriter(skill1, 5);
        typewriter(skill2, 5);
        typewriter(skill3, 5);
        System.out.println();
    }

    @Override
    public void takeTurn(GameCharacter target) {
        int choice = random.nextInt(3) + 1;
        switch (choice) {
            case 1 -> skill1(target);
            case 2 -> skill2(target);
            case 3 -> skill3(target);
        }
    }

    //this is when a character is controlled by the computer.
    @Override
    public void autoTakeTurn(GameCharacter target) {
        int choice = random.nextInt(3) + 1;
        if(choice == 1 && skill1Cooldown > 0) {
            choice = (skill2Cooldown == 0) ? 2 : 3;
        } else if(choice == 2 && skill2Cooldown > 0) {
            choice = (skill3Cooldown == 0) ? 3 : 1;
        } else if(choice == 3 && skill3Cooldown > 0) {
            choice = (skill1Cooldown == 0) ? 1 : 2;
        }
        switch (choice) {
            case 1 -> skill1(target);
            case 2 -> skill2(target);
            case 3 -> skill3(target);
        }
    }

    public void typewriter(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
}