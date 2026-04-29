package character;

public class SirKhai extends GameCharacter {
    public SirKhai() {
        super("Sir Khai", "The legendary goat, undefeated champion of the arena...", 2500, 1500);

        this.addSkill(new Skill("Divine Slash", 1, "600 Base Dmg. Cost: 200MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 200) return "Not enough mana!";
                source.useMana(200);
                int dmg = target.takeDamage(source.randomDamage(10000000, 30));
                return source.getName() + " unleashes DIVINE SLASH! Dealt " + dmg + " damage!";
            }
        });

        this.addSkill(new Skill("Eternal Guardian", 3, "Become untargetable for 1 turn. Cost: 400MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 400) return "Not enough mana!";
                source.useMana(400);
                source.setUntargetable(true);
                source.setStatusEffectTurns(1);
                return source.getName() + " activates ETERNAL GUARDIAN! Cannot be targeted!";
            }
        });

        this.addSkill(new Skill("Goat's Blessing", 5, "Heal 60% HP and +75% damage for 3 turns. Cost: 600MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 600) return "Not enough mana!";
                source.useMana(600);
                int healAmount = (int)(source.getMaxHealth() * 0.6);
                source.heal(healAmount);
                source.setAttackBonus(1.75);
                source.setStatusEffectTurns(3);
                return "GOAT'S BLESSING descends! Healed " + healAmount + " HP and gained +75% damage for 3 turns!";
            }
        });
    }
}
