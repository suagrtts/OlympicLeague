package character;

public class Vor extends GameCharacter {
    public Vor() {
        super("Vor", "Incarnation of Keith, God of Time...", 1700, 1000);

        this.addSkill(new Skill("Time Slash", 1, "300 Base Dmg. Cost: 150MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 150) return "Not enough mana!";
                source.useMana(150);
                return "TIME SLASH cuts through reality! Dealt " + target.takeDamage(source.randomDamage(300, 20)) + " damage!";
            }
        });

        this.addSkill(new Skill("Temporal Shift", 2, "Evade next attack. Cost: 120MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 120) return "Not enough mana!";
                source.useMana(120);
                source.setUntargetable(true);
                source.setStatusEffectTurns(1);
                return "TEMPORAL SHIFT! Time freezes, next attack will be evaded!";
            }
        });

        this.addSkill(new Skill("Chrono Mark", 5, "+25% Dmg for 2 turns. Cost: 500MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 500) return "Not enough mana!";
                source.useMana(500);
                source.setAttackBonus(1.25);
                source.setStatusEffectTurns(2);
                return "CHRONO MARK applied! Damage increased by 25% for 2 turns!";
            }
        });
    }
}