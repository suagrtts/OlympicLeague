package Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCharacter implements Damageable, MagicUser, Combatant {
    private final String name;
    private final String backstory;
    private int health;
    private final int maxHealth;
    private int mana;
    private final int maxMana;
    private boolean isAlive;
    protected Random random;

    private final List<Skill> skills;

    private double attackBonus = 1.0;
    private double defenseBonus = 1.0;
    private int statusEffectTurns = 0;

    private boolean untargetable = false;
    private boolean isStunned = false;
    private boolean hasEscaped = false;

    public GameCharacter(String name, String backstory, int health, int mana) {
        this.name = name;
        this.backstory = backstory;
        this.health = health;
        this.maxHealth = health;
        this.mana = mana;
        this.maxMana = mana;
        this.isAlive = true;
        this.random = new Random();
        this.skills = new ArrayList<>();
    }

    public void addSkill(Skill skill) { this.skills.add(skill); }
    public List<Skill> getSkills() { return skills; }

    // Combat Math
    protected int randomDamage(int baseDamage, int variance) {
        int minDamage = Math.max(1, baseDamage - variance);
        int maxDamage = baseDamage + variance;
        int damage = random.nextInt(maxDamage - minDamage + 1) + minDamage;
        damage = (int)(damage * (0.85 + (0.3 * random.nextDouble())));
        return (int)(damage * attackBonus);
    }

    @Override
    public int takeDamage(int damage) {
        if (!this.isAlive || this.untargetable) return 0;

        int actualDamage = damage;
        if (this.statusEffectTurns > 0 && this.defenseBonus != 1.0) {
            actualDamage = (int)(damage * this.defenseBonus);
        }

        this.health = Math.max(0, this.health - actualDamage);
        if (this.health == 0) this.isAlive = false;
        return actualDamage;
    }

    @Override
    public int takeTrueDamage(int damage) {
        if (!this.isAlive) return 0;
        this.health = Math.max(0, this.health - damage);
        if (this.health == 0) this.isAlive = false;
        return damage;
    }

    @Override
    public void heal(int amount) { this.health = Math.min(this.health + amount, this.maxHealth); }
    @Override
    public void useMana(int amount) { this.mana = Math.max(0, this.mana - amount); }
    @Override
    public void restoreMana(int amount) { this.mana = Math.min(this.mana + amount, this.maxMana); }

    @Override
    public void updateTurnEffects() {
        for (Skill skill : skills) skill.reduceCooldown();
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
        for (Skill skill : skills) skill.resetCooldown();
    }

    @Override
    public String takeTurn(GameCharacter target, int skillIndex) {
        Skill chosenSkill = skills.get(skillIndex);
        chosenSkill.putOnCooldown();
        return chosenSkill.execute(this, target);
    }

    @Override
    public String autoTakeTurn(GameCharacter target) {
        List<Skill> available = new ArrayList<>();
        for (Skill s : skills) if (s.isReady()) available.add(s);
        if (available.isEmpty()) return name + " passes their turn.";

        Skill chosen = available.get(random.nextInt(available.size()));
        chosen.putOnCooldown();
        return chosen.execute(this, target);
    }

    // Getters / Setters
    public String getName() { return name; }
    public String getBackstory() { return backstory; }
    @Override public int getHealth() { return health; }
    @Override public int getMaxHealth() { return maxHealth; }
    @Override public boolean isAlive() { return isAlive; }
    @Override public int getMana() { return mana; }
    @Override public int getMaxMana() { return maxMana; }
    public double getAttackBonus() { return attackBonus; }
    public void setAttackBonus(double attackBonus) { this.attackBonus = attackBonus; }
    public double getDefenseBonus() { return defenseBonus; }
    public void setDefenseBonus(double defenseBonus) { this.defenseBonus = defenseBonus; }
    public int getStatusEffectTurns() { return statusEffectTurns; }
    public void setStatusEffectTurns(int turns) { this.statusEffectTurns = turns; }
    public boolean isUntargetable() { return untargetable; }
    public void setUntargetable(boolean untargetable) { this.untargetable = untargetable; }
    public boolean isStunned() { return isStunned; }
    public void setStunned(boolean stunned) { this.isStunned = stunned; }
    public boolean hasEscaped() { return hasEscaped; }
    public void setEscaped(boolean escaped) { this.hasEscaped = escaped; }

    public void showInfo() {
        System.out.println("\n==== " + this.name + " ====");
        System.out.println("Backstory: " + this.backstory);
        System.out.println("Health: " + this.maxHealth);
        System.out.println("Mana: " + this.maxMana);
        System.out.println("Skills:");
        for (int i = 0; i < this.skills.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + this.skills.get(i).getName() + " - " + this.skills.get(i).getDescription());
        }
        System.out.println();
    }
}