package character;

public class Achiron extends GameCharacter {
    public Achiron() {
        super("Achiron", "The Unyielding Warrior...", 1800, 1000);

        this.addSkill(new Skill("Spear Thrust", 1, "400 Base Dmg. Cost: 180MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 180) return "Not enough mana!";
                source.useMana(180);
                int dmg = target.takeDamage(source.randomDamage(400, 20));
                return source.getName() + " uses SPEAR THRUST! Dealt " + dmg + " damage!";
            }
        });

        this.addSkill(new Skill("Aegis Shield", 3, "Reduce next damage by 50%. Cost: 320MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 320) return "Not enough mana!";
                source.useMana(320);
                source.setDefenseBonus(0.5);
                source.setStatusEffectTurns(1);
                return source.getName() + " uses AEGIS SHIELD! Incoming damage halved!";
            }
        });

        this.addSkill(new Skill("Wrath of Ares", 5, "+50% damage for 2 turns. Cost: 500MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 500) return "Not enough mana!";
                source.useMana(500);
                source.setAttackBonus(1.5);
                source.setStatusEffectTurns(2);
                return "WRATH OF ARES ACTIVATED! +50% damage for 2 turns!";
            }
        });
    }

    @Override
    public int takeDamage(int damage) {
        boolean hadShield = (this.getStatusEffectTurns() > 0 && this.getDefenseBonus() == 0.5);
        int actual = super.takeDamage(damage);
        if (hadShield && actual > 0) {
            this.setDefenseBonus(1.0);
            this.setStatusEffectTurns(0);
        }
        return actual;
    }
}