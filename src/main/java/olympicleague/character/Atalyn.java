package character;

public class Atalyn extends GameCharacter {
    public Atalyn() {
        super("Atalyn", "Hunter of Artemis...", 1500, 950);

        this.addSkill(new Skill("Piercing Arrow", 1, "360 Base Dmg. Cost: 150MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 150) return "Not enough mana!";
                source.useMana(150);
                return "PIERCING ARROW hits for " + target.takeDamage(source.randomDamage(360, 18)) + " damage!";
            }
        });

        this.addSkill(new Skill("Hunter's Reflex", 2, "Evade next attack. Cost: 120MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 120) return "Not enough mana!";
                source.useMana(120);
                source.setUntargetable(true);
                source.setStatusEffectTurns(1);
                return "HUNTER'S REFLEX active! Next attack will be evaded!";
            }
        });

        this.addSkill(new Skill("Moonlit Mark", 5, "+50% Damage for 2 turns. Cost: 500MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 500) return "Not enough mana!";
                source.useMana(500);
                source.setStatusEffectTurns(2);
                source.setAttackBonus(1.5);
                return "MOONLIT MARK cast! +50% damage for 2 turns!";
            }
        });
    }
}